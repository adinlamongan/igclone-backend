package com.adn.cloneig.controllers;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.LoginRequest;
import com.adn.cloneig.dto.RegisterRequestDTO;
import com.adn.cloneig.model.AppUser;
import com.adn.cloneig.security.jwt.JwtUtils;
import com.adn.cloneig.security.service.RefreshTokenService;
import com.adn.cloneig.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private AppUserService appUserService;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;
    private AppProperties appProperties;


    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO dto) {
        appUserService.registerUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser userDetails = (AppUser) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        String image = userDetails.getImage() == null ? appProperties.getImgProfileDefault()
                : appProperties.getProfileStorage() + userDetails.getImage();
        Map<String, Object> userResponse = new LinkedHashMap<>();
        userResponse.put("id", userDetails.getId());
        userResponse.put("username", userDetails.getUsername());
        userResponse.put("email", userDetails.getEmail());
        userResponse.put("image", image);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("token", jwt );
        responseBody.put("type", "Bearer");
        responseBody.put("user", userResponse );
        return ResponseEntity.ok(responseBody);

    }





}
