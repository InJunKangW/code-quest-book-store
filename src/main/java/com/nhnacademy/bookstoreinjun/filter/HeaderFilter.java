package com.nhnacademy.bookstoreinjun.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class HeaderFilter extends OncePerRequestFilter {
    private final String requiredPath;
    private final String requiredMethod;
    private final String[] requiredRole;
    private final AntPathMatcher pathMatcher;

    public HeaderFilter(String requiredPath, String requiredMethod) {
        this.requiredRole = new String[0];
        this.requiredPath = requiredPath;
        this.requiredMethod = requiredMethod;
        this.pathMatcher = new AntPathMatcher();
    }

    public HeaderFilter(String requiredPath, String requiredMethod, String... requiredRole) {
        this.requiredRole = requiredRole;
        this.requiredPath = requiredPath;
        this.requiredMethod = requiredMethod;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("header filter start");
        if (pathMatcher.match(requiredPath, request.getRequestURI()) && request.getMethod().equalsIgnoreCase(requiredMethod)) {
            try {
                Long.valueOf(request.getHeader("X-User-Id"));
            } catch ( NumberFormatException e ) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "id header is missing or invalid");
                return;
            }

            if (!isContainRole(getHeaderValues(request, "X-User-Role"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role header is missing or invalid");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Set<String> getHeaderValues(HttpServletRequest request, String headerName) {
        Set<String> headerValues = new HashSet<>();
        Enumeration<String> headers = request.getHeaders(headerName);
        while (headers.hasMoreElements()) {
            headerValues.add(headers.nextElement());
        }
        return headerValues;
    }

    private boolean isContainRole(Set<String> roleSet) {
        for (String role : requiredRole) {
            if (!roleSet.contains(role)) {
                return false;
            }
        }
        return true;
    }
}