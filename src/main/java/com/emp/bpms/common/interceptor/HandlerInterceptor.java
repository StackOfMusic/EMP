package com.emp.bpms.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.emp.bpms.repository.user.UserDTO;
import com.emp.bpms.repository.user.UserService;

@Component
public class HandlerInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;
    
    public static boolean isUserLogged() {
        try {
            return !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        if (viewName.startsWith("redirect:/")) {
            return true;
        }
        View view = modelAndView.getView();
        return (view != null && view instanceof SmartView
          && ((SmartView) view).isRedirectView());
    }
    
    private void addToModelUserDetails(ModelAndView modelAndView) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO loginUser = userService.loadUserByUsername(loginId);
        loginUser.setPwd(null);
        modelAndView.addObject("loginUser", loginUser);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            if (isUserLogged()) {
                addToModelUserDetails(modelAndView);
            }
        }
    }
}
