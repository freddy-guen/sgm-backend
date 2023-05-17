package fr.guen.dev.sgm.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static fr.guen.dev.sgm.common.constants.Constants.*;
import static fr.guen.dev.sgm.common.enums.Role.*;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CostumerUsersDetailsService costumerUsersDetailsService;

    Claims claims = null;
    private String username = null;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(httpServletRequest.getServletPath().matches(PATH_MATCHES)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
            String token = null;
            if(authorizationHeader != null && authorizationHeader.startsWith(BEARER_WITH_BLANK_AT_THE_END)){
                token = authorizationHeader.substring(INT_7);
                username = jwtUtils.extractUsername(token);
                claims = jwtUtils.extractAllClaims(token);
            }
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = costumerUsersDetailsService.loadUserByUsername(username);
                if(Boolean.TRUE.equals(jwtUtils.validateToken(token, userDetails))) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public boolean isAdmin() {
        return ADMIN_ROLE.toString().equalsIgnoreCase((String) claims.get(ROLE));
    }

    public boolean isUser() {
        return USER_ROLE.toString().equalsIgnoreCase((String) claims.get(ROLE));
    }

    public String getCurrentUser() {
        return username;
    }
}
