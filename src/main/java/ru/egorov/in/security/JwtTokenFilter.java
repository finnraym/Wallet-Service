package ru.egorov.in.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import ru.egorov.model.Player;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

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
        servletRequest.setCharacterEncoding("UTF-8");
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenProvider.validateToken(bearerToken.substring(7))) {
            try {
                Player authPlayer = jwtTokenProvider.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authPlayer);
            } catch (AccessDeniedException e) {
                servletContext.setAttribute("authentication", null);
            }
        } else {
            servletContext.setAttribute("authentication", null);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
