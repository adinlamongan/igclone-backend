package com.adn.cloneig.services;

import com.adn.cloneig.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    int addPost(PostRequestDTO dto, MultipartFile multipartFile) throws IOException;
    int addComment(PostAddCommentRequestDTO dto);

    void likePost(PostLikeRequestDTO dto);

    PageResponseDTO findPost(Integer pageNo, Integer limit);


    List<PostCommentResponseDTO> getMoreComments(int postId, int commentId);
}
