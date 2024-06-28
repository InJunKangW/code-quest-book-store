package com.nhnacademy.bookstoreinjun.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class RoleHeaderFilter extends OncePerRequestFilter {
    private final String requiredPath;
    private final String requiredMethod;
    private final String requiredRole;
    private final AntPathMatcher pathMatcher;

    public RoleHeaderFilter(String requiredPath, String requiredMethod, String requiredRole) {
        this.requiredRole = requiredRole;
        this.requiredPath = requiredPath;
        this.requiredMethod = requiredMethod;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (pathMatcher.match(requiredPath, request.getRequestURI()) && request.getMethod().equalsIgnoreCase(requiredMethod)) {
            log.info("role filter start, real path: {} required path: {}, method : {}, realMethod: {}", requiredPath, request.getRequestURI(), requiredMethod, request.getMethod());
            try {
                Long.valueOf(request.getHeader("X-User-Id"));
            } catch ( NumberFormatException e ) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "id header is missing or invalid");
                return;
            }

            String role = request.getHeader("X-User-Role");
            log.info("role filter end, role: {}", role);
            if (role == null || role.isEmpty() || !role.equals(requiredRole)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Role header is missing or invalid");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
