package org.base.oauth2.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.oauth2.config.JwtTokenSetup;
import org.base.oauth2.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/access")
public class AccessController {

    private final JwtTokenSetup jwtTokenSetup;

    @Value("${secret.time_live}")
    private Long timeLive;

    @Autowired
    public AccessController(JwtTokenSetup jwtTokenSetup) {
        this.jwtTokenSetup = jwtTokenSetup;
    }

    @GetMapping("/get-token")
    public ResponseEntity getToken(@Valid @RequestParam String account) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication != null) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();

            // Kiểm tra xem người dùng có trùng khớp với tên người dùng không
            if (userDetails.getAttribute("login").equals(account)) {
                String token = jwtTokenSetup.generateToken(account);
                return ResponseEntity.ok(new TokenDTO(token, timeLive));
            }
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/user")
    public ResponseEntity checkGitHubLoginStatus(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return ResponseEntity.ok(oAuth2User);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/redirect-to-react-app")
    public ResponseEntity<Void> redirectToGoogleWithResponseEntity(@AuthenticationPrincipal OAuth2User oAuth2User) {
        // Specify the Google URL you want to redirect to
        String googleUrl = oAuth2User != null ? "http://localhost:5173?username=" + oAuth2User.getAttribute("login") :
                                            "https://www.google.com";
        // Create a RedirectView with the Google URL
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(googleUrl);

        // Return a ResponseEntity with a Location header
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(googleUrl))
                .build();
    }
}
