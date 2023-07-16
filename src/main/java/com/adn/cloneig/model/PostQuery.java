package com.adn.cloneig.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostQuery {

    private int id;
    private int userId;
    private String username;
    private String caption;
    private String image;
    private int jmlLike;
    private int jmlComment;
    private LocalDateTime createdAt;
    private Boolean likeState;
    private String komentar;
    private int userIdKomen;
    private String usernameKomen;
    private LocalDateTime createdAtKomen;

}
