package com.adn.cloneig.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Table("stories")
public class Stories {
    @Id
    private int id;
    private int userId;
    private String judul;
    private String deskripsi;
    private String image;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
