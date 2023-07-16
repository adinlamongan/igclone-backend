package com.adn.cloneig.repository;

import com.adn.cloneig.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepo {

    Page<PostResponseDTO> getPosts(int userId, Pageable page);

}
