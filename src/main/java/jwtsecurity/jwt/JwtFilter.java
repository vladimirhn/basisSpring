package jwtsecurity.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import jwtsecurity.userdetails.CustomUserDetails;
import jwtsecurity.userdetails.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("a".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null){
            token = getTokenFromRequest(request);
        }

        if (token != null && jwtProvider.validateToken(token)) {
            try {

                String userId = jwtProvider.parseToken(token).getSubject();
//                Date expiration = jwtProvider.parseToken(token).getExpiration();
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {

                final ResponseCookie responseCookie = ResponseCookie
                        .from("a", token)
                        .secure(true)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(0)
                        .sameSite("Strict")
                        .build();
                ((HttpServletResponse)servletResponse).addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}