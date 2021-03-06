package io.github.thesixonenine.auth.web;

import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.auth.vo.UserLoginVO;
import io.github.thesixonenine.auth.vo.UserRegisterVO;
import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.member.controller.MemberController;
import io.github.thesixonenine.member.entity.MemberEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/09 19:05
 * @since 1.0
 */
@Controller
public class IndexController {
    private static final String SMS_CODE_REDIS_PREFIX = "sms:code:";
    private static final String SMS_CODE_REDIS_VALUE_SPLIT = "_";
    private static final Long SMS_CODE_REDIS_CACHE_TIME = 10L;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberController memberController;

    @Value(value = "${auth.sms-code}")
    private String smsCode;

    @GetMapping(value = "/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam(value = "phone") String phone) {
        String key = SMS_CODE_REDIS_PREFIX + phone;

        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(s)) {
            long l = Long.parseLong(s.split(SMS_CODE_REDIS_VALUE_SPLIT)[1]);
            if (System.currentTimeMillis() - l < 60 * 1000) {
                // 60秒内不能再发
                return R.error(10002, "60秒内不能再发验证码");
            }
        }


        String value = smsCode + SMS_CODE_REDIS_VALUE_SPLIT + System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, value, SMS_CODE_REDIS_CACHE_TIME, TimeUnit.MINUTES);
        return R.ok();
    }

    /**
     * RedirectAttributes 重定向也可以携带数据, 实则是利用session, 将数据放入session中 TODO 分布式session问题
     * consumes = "application/x-www-form-urlencoded;charset=UTF-8"
     *
     * @param userRegisterVO
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping(value = "/register")
    public String reg(@Valid UserRegisterVO userRegisterVO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> map = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.jdmall.com/reg";
        }
        // 校验验证码
        String code = userRegisterVO.getCode();

        String phone = userRegisterVO.getPhone();
        String key = SMS_CODE_REDIS_PREFIX + phone;
        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(s)) {
            String smsCode = s.split(SMS_CODE_REDIS_VALUE_SPLIT)[0];
            if (StringUtils.equalsIgnoreCase(smsCode, code)) {
                // 验证码通过
                redisTemplate.delete(key);
                // 调用会员服务进行注册
                R r = memberController.register(userRegisterVO.getUsername(), userRegisterVO.getPassword(), phone);
                if (r.getCode() == 0) {
                    return "redirect:http://auth.jdmall.com/login";
                } else {
                    Map<String, String> map = new HashMap<>(1);
                    map.put("msg", r.getData(new TypeToken<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", map);
                    return "redirect:http://auth.jdmall.com/reg";
                }
            } else {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", "验证码不正确");
                redirectAttributes.addFlashAttribute("errors", map);
                return "redirect:http://auth.jdmall.com/reg";
            }
        } else {
            Map<String, String> map = new HashMap<>(1);
            map.put("code", "验证码已过期");
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.jdmall.com/reg";
        }
    }

    @PostMapping(value = "/signin")
    public String login(@Valid UserLoginVO userLoginVO, RedirectAttributes redirectAttributes, HttpSession session) {
        // 远程登录
        R r = memberController.login(userLoginVO.getUsername(), userLoginVO.getPassword());
        if (r.getCode() != 0) {
            Map<String, String> map = new HashMap<>(1);
            map.put("msg", (String) r.get("msg"));
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.jdmall.com/login";
        }
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) r.get("data");
        MemberEntity member = new MemberEntity();
        Integer id = (Integer) data.get("id");
        Integer levelId = (Integer) data.get("levelId");
        String username = (String) data.get("username");
        String mobile = (String) data.get("mobile");

        member.setId(Long.valueOf(id));
        member.setLevelId(Long.valueOf(levelId));
        member.setUsername(username);
        member.setMobile(mobile);
        session.setAttribute(Constant.LOGIN_USER, member);
        return "redirect:http://jdmall.com";
    }

    @GetMapping(value = "/login")
    public String loginPage(HttpSession session) {
        // 如果已登录, 则直接去首页
        Object o = session.getAttribute(Constant.LOGIN_USER);
        if (Objects.isNull(o)) {
            // 没登录, 去登录
            return "login";
        }
        return "redirect:http://jdmall.com";
    }
}
