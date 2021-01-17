package com.example.renting.interceptor;

import com.example.renting.annotation.AdminPrivileged;
import com.example.renting.annotation.ClientPrivileged;
import com.example.renting.annotation.NoTokenRequired;
import com.example.renting.annotation.RealtorPrivileged;
import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.service.AuthService;
import com.example.renting.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private AuthService authService;

    private NoTokenRequired getNoTokenRequiredAnnotation(Object handler) {

        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getAnnotation(NoTokenRequired.class);
        }
        return null;
    }

    private AdminPrivileged getAdminPrivilegedAnnotation(Object handler) {

        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getAnnotation(AdminPrivileged.class);
        }
        return null;
    }

    private ClientPrivileged getClientPrivilegedAnnotation(Object handler) {

        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getAnnotation(ClientPrivileged.class);
        }
        return null;
    }

    private RealtorPrivileged getRealtorPrivilegedAnnotation(Object handler) {

        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getAnnotation(RealtorPrivileged.class);
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("=== RECEIVED {} {} from {}", request.getMethod(), request.getRequestURI(),
                request.getAttribute("clientIP"));

        NoTokenRequired noTokenRequired = getNoTokenRequiredAnnotation(handler);
        if(noTokenRequired != null)
            return true;

        String token = request.getHeader("token");

        User.Role role = authService.getUserRole(token);

        ClientPrivileged clientPrivileged = getClientPrivilegedAnnotation(handler);
        if(clientPrivileged != null)
            return true;

        RealtorPrivileged realtorPrivileged = getRealtorPrivilegedAnnotation(handler);
        if(realtorPrivileged != null) {
            if(role.equals(User.Role.REALTOR) || role.equals(User.Role.ADMIN))
                return true;
            throw ForbiddenException.ex("You don't have the permission for this action");
        }

        AdminPrivileged adminPrivileged = getAdminPrivilegedAnnotation(handler);
        if(adminPrivileged != null) {
            if(role.equals(User.Role.ADMIN))
                return true;
            throw ForbiddenException.ex("You don't have the permission for this action");
        }

        return true;
    }
}
