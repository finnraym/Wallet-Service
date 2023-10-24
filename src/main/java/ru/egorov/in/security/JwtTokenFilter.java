package ru.egorov.in.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter
public class JwtTokenFilter implements Filter {

    private JwtTokenProvider jwtTokenProvider;
    private ServletContext servletContext;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        jwtTokenProvider = (JwtTokenProvider) servletContext.getAttribute("tokenProvider");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenProvider.validateToken(bearerToken.substring(7))) {
                Authentication authentication = jwtTokenProvider.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authentication);
            } else {
                servletContext.setAttribute("authentication", new Authentication(null, false, "Bearer token is null or invalid!"));
            }
        } catch (RuntimeException e) {
            servletContext.setAttribute("authentication", new Authentication(null, false, e.getMessage()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
