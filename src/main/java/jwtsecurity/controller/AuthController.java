package jwtsecurity.controller;

import jwtsecurity.jwt.JwtProvider;
import jwtsecurity.user.User;
import jwtsecurity.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class AuthController {
    @Autowired
    private UsersService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        userService.saveUser(u);
        return "OK";
    }

    @PostMapping("/auth")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public AuthResponse auth(@RequestBody AuthRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {

        long authDaysDuration = 15 * 24 * 60 * 60;
        long authDaysDurationMillies = authDaysDuration * 1000;
        long expMillies = new Date().getTime() + authDaysDurationMillies;

        Date expDate = new Date(expMillies);

        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin(), expDate);

        final ResponseCookie responseCookie = ResponseCookie
                .from("a", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new AuthResponse(user.getLogin(), String.valueOf(expMillies));
    }
}