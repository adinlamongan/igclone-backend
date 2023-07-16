package com.adn.cloneig.controllers;

import com.adn.cloneig.dto.*;
import com.adn.cloneig.services.FollowerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FollowerController {

    private final FollowerService followerService;

    @GetMapping("/follow/suggestion")
    public ResponseEntity<List<SuggestionsResponseDTO>> getSuggestionUser() {
        List<SuggestionsResponseDTO> result = followerService.getSuggestionsUser();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> followUser(@Valid @RequestBody FollowRequestDTO dto)  {
        followerService.followUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
