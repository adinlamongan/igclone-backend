package com.adn.cloneig.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@Table("posts")
@NoArgsConstructor
public class Posts {
	@Id
    private int id;
	private int userId;
	private String caption;
	private String image;
	private int jmlLike;
	private int jmlComment;
	private LocalDateTime createdAt=LocalDateTime.now().withNano(0);
}
