package com.adn.cloneig.controllers;


import com.adn.cloneig.dto.PostRequestDTO;
import com.adn.cloneig.dto.ProfileResponseDTO;
import com.adn.cloneig.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private AppUserService appUserService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<Object> userInfo(@PathVariable("username") String username) {
        ProfileResponseDTO dto = appUserService.getProfile(username);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/profile/{username}")
    public ResponseEntity<Void> userInfo(@PathVariable("username") String username, @RequestBody @RequestParam("image") MultipartFile multipartFile,
                                           PostRequestDTO dto) throws IOException {
        appUserService.editProfile(username, multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
