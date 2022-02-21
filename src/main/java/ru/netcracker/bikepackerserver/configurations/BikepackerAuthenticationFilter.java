package ru.netcracker.bikepackerserver.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.service.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class BikepackerAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static String authRequestFieldEmail = "email";
    private static String authRequestFieldPassword = "password";

    @Autowired
    private final ObjectMapper objectMapper;
    private final Gson gson = new Gson();

    protected BikepackerAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.objectMapper = objectMapper;
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl authenticatedUser = (UserDetailsImpl) authentication.getPrincipal();
            response.getWriter().write(gson.toJson(UserModel.toModel(authenticatedUser)));
        });
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        String email, password;

        try {
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);
            email = requestMap.get(authRequestFieldEmail);
            password = requestMap.get(authRequestFieldPassword);
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
        return authentication;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
