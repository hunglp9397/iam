package com.hunglp.iambackend.intercepter;

import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.service.TenantService;
import com.hunglp.iambackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Configuration
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Intercepting the request");
        System.out.println("---------");
        String requestURI = request.getRequestURI();
        String tenantId = request.getHeader("X-TenantID");
        System.out.println("Request URI :: " + requestURI + " -- TenantID :: " + tenantId);

        if (tenantId == null) {
            response.getWriter().write("X-TenantID not present in the Request Header");
            response.setStatus(400);
            return false;
        }

        Optional<Tenant> tenant = tenantService.findByTenantName(tenantId);
        if (tenant.isPresent()) {
            TenantContext.setCurrentTenant(tenantId);
            return true;
        } else {
            response.getWriter().write("X-TenantID not exist or deleted");
            response.setStatus(400);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContext.clear();
    }
}
