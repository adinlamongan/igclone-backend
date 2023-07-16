package com.adn.cloneig.controllers;

import com.adn.cloneig.dto.*;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.adn.cloneig.services.PostService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<PageResponseDTO> listPostsdata(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "3", required = false) int limit) {
        PageResponseDTO result = postService.findPost(page, limit);
        return ResponseEntity.ok().body(result);

    }

    @PostMapping("/posts")
    public ResponseEntity<Object> addPosts(@Valid @RequestBody @RequestParam("image") MultipartFile multipartFile,
            PostRequestDTO dto) throws IOException  {
        int id = postService.addPost(dto, multipartFile);
        Map<String, Object> userResponse = new LinkedHashMap<>();
        userResponse.put("id", id);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/posts/add_comments")
    public ResponseEntity<Object> addCommentPost(@Valid @RequestBody PostAddCommentRequestDTO dto)  {
        int id = postService.addComment(dto);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("id", id);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/posts/like")
    public ResponseEntity<Void> likePost(@Valid @RequestBody PostLikeRequestDTO dto) {
        postService.likePost(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/more_comments")
    public ResponseEntity<List<PostCommentResponseDTO>> getMoreComments(
            @RequestParam(name = "comment_id", required = true) int commentId,
            @RequestParam(name = "post_id", required = true) int postId) {
        List<PostCommentResponseDTO> result = postService.getMoreComments(postId, commentId);
        return ResponseEntity.ok().body(result);

    }
}
