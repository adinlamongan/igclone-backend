package com.adn.cloneig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("followers")
public class Followers {

    @Id
    private int id;
    private int userId;
    private int followerUserId;
    private LocalDateTime createdAt=LocalDateTime.now().withNano(0);
}
