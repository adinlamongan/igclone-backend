package com.adn.cloneig.repository;

import com.adn.cloneig.model.LikePost;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikePostRepo extends CrudRepository<LikePost, Integer> {

    LikePost findByUserIdAndPostId(int userId, int postId);
}
