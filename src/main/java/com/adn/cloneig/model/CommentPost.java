package com.adn.cloneig.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("comment_post")
public class CommentPost {

    @Id
    private int id;
    private int userId;
    private int postId;
    private String komentar;
	private LocalDateTime createdAt = LocalDateTime.now().withNano(0);
}
