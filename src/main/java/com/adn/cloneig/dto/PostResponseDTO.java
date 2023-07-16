package com.adn.cloneig.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponseDTO {
    private int id;
    private int userId;
    private String caption;
    private String image;
    private int jmlLike;
    private int jmlComment;
    private LocalDateTime createdAt;
    private String username;
    private Boolean likeState;
    private String imageProfile;

    private List<PostCommentResponseDTO> comments;


}
