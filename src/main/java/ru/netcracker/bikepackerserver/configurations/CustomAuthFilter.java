package ru.netcracker.bikepackerserver.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CustomAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    protected CustomAuthFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        String email, password;

        try {
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);
            email = requestMap.get("email");
            password = requestMap.get("password");
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
        return authentication;
    }
}
