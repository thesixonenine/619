package io.github.thesixonenine.cart.interceptor;

import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.member.entity.MemberEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/10/27 22:39
 * @since 1.0
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<Pair<Long, String>> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long id = null;
        MemberEntity member = (MemberEntity) session.getAttribute(Constant.LOGIN_USER);
        if (Objects.nonNull(member)) {
            // 已登录
            id = member.getId();
        }
        String userKey = StringUtils.EMPTY;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(Constant.USER_KEY, cookie.getName())) {
                    userKey = cookie.getValue();
                }
            }
        }
        if (StringUtils.isBlank(userKey)) {
            userKey = UUID.randomUUID().toString();
        }
        threadLocal.set(Pair.of(id, userKey));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Cookie cookie = new Cookie(Constant.USER_KEY, threadLocal.get().getRight());
        cookie.setDomain("jdmall.com");
        // 一个月
        cookie.setMaxAge(2678400);
        response.addCookie(cookie);
    }
}
