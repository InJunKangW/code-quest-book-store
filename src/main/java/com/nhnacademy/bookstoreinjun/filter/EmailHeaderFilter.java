//package com.nhnacademy.bookstoreinjun.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
//import java.io.IOException;
//
//@Slf4j
//public class EmailHeaderFilter extends AbstractPreAuthenticatedProcessingFilter {
//
//    private static final String EMAIL_HEADER = "email";
//    private static final String ROLE_HEADER = "role";
//
//    public EmailHeaderFilter(AuthenticationManager authenticationManager) {
//        setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
//        return request.getHeader(EMAIL_HEADER);
//    }
//
//    @Override
//    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
//        return request.getHeader(ROLE_HEADER);
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        log.info("Email header filter started");
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String email = httpRequest.getHeader(EMAIL_HEADER);
//        String role = httpRequest.getHeader(ROLE_HEADER);
//
//        if (email == null || email.isEmpty() || role == null || role.isEmpty()) {
//            log.info("Email header filter ended");
//            httpResponse.sendError(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Email or Role header is missing or empty");
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//}