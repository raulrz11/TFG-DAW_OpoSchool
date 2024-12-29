package com.example.tfgoposchool.config.auth;

import com.example.tfgoposchool.auth.services.jwt.JwtServiceImpl;
import com.example.tfgoposchool.auth.services.users.AuthUsersServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtServiceImpl jwtService;
    private final AuthUsersServiceImpl authUsersService;

    @Autowired
    public JwtAuthFilter(JwtServiceImpl jwtService, AuthUsersServiceImpl authUsersService) {
        this.jwtService = jwtService;
        this.authUsersService = authUsersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails;
        String username;
        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no autorizado o no válido");
            return;
        }

        if (StringUtils.hasText(username)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Comprobamos que el usuario existe y que el token es válido
            try {
                userDetails = authUsersService.loadUserByUsername(username);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autorizado");
                return;
            }
            authUsersService.loadUserByUsername(username);
            if (jwtService.tokenValid(jwt, userDetails)) {
                // Si es válido, lo autenticamos en el contexto de seguridad
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // Añadimos los detalles de la petición
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Lo añadimos al contexto de seguridad
                context.setAuthentication(authToken);
                // Y lo añadimos al contexto de seguridad
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
