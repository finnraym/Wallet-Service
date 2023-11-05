package ru.egorov.in.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.*;
import java.io.IOException;

/**
 * Jwt token filter for authentication
 */
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    /**
     * Validate jwt token and authentication player
     *
     * @param servletRequest servlet request
     * @param servletResponse servlet response
     * @param filterChain filter chain
     * @throws IOException
     * @throws ServletException
     * @throws ExpiredJwtException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ExpiredJwtException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
            Authentication authentication = jwtTokenProvider.authentication(bearerToken);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
