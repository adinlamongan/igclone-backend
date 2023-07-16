package com.adn.cloneig.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Table("like_post")
public class LikePost {

    @Id
    private int id;
    private int userId;
    private int postId;
    private Boolean state;
    private LocalDateTime createdAt=LocalDateTime.now().withNano(0);
}
