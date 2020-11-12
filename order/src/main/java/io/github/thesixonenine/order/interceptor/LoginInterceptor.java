package io.github.thesixonenine.order.interceptor;

import io.github.thesixonenine.common.utils.Constant;
import io.github.thesixonenine.member.entity.MemberEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/11 22:25
 * @since 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<Pair<Long, String>> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberEntity member = (MemberEntity) session.getAttribute(Constant.LOGIN_USER);
        if (member == null) {
            session.setAttribute("msg", "请先进行登录");
            response.sendRedirect("http://auth.jdmall.com/login");
            return false;
        }
        threadLocal.set(Pair.of(member.getId(), null));
        return true;
    }
}
