package jwtsecurity.controller;

import jwtsecurity.jwt.JwtProvider;
import jwtsecurity.user.User;
import jwtsecurity.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

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
//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public AuthResponse registerUser(@RequestBody RegistrationRequest registrationRequest,
                               HttpServletRequest httpRequest, HttpServletResponse response) {

        if (userService.isLoginExists(registrationRequest.getLogin())) {
            return null;
        }

        User newbie = userService.createUser(registrationRequest.getLogin(), registrationRequest.getPassword());

        return makeSuccessAuthResponse(response, newbie);
    }

    @PostMapping("/auth")
//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public AuthResponse auth(@RequestBody AuthRequest request,
                             HttpServletRequest httpRequest, HttpServletResponse response) {


        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());

        if (user == null) throw new IllegalStateException("no user");

        return makeSuccessAuthResponse(response, user);
    }

    private AuthResponse makeSuccessAuthResponse(HttpServletResponse response, User newbie) {

        long authDaysDuration = 15 * 24 * 60 * 60;
        long authDaysDurationMillies = authDaysDuration * 1000;
        long expMillies = new Date().getTime() + authDaysDurationMillies;

        Date expDate = new Date(expMillies);

        String token = jwtProvider.generateToken(newbie.getId(), expDate);

        final ResponseCookie responseCookie = ResponseCookie
                .from("a", token)
//                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new AuthResponse(newbie.getLogin(), String.valueOf(expMillies));
    }

    @GetMapping("/u/logout")
//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public void logout(HttpServletRequest httpRequest, HttpServletResponse response) {

        final ResponseCookie responseCookie = ResponseCookie
                .from("a", "")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(1)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}