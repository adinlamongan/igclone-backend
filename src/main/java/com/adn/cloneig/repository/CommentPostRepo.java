package com.adn.cloneig.repository;

import com.adn.cloneig.dto.PostCommentResponseDTO;
import com.adn.cloneig.model.CommentPost;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentPostRepo extends CrudRepository<CommentPost, Integer> {

    @Query("    SELECT  "
            + "      cp.*,  "
            + "      mu.username  "
            + "    FROM  "
            + "      comment_post cp  "
            + "      JOIN master_users mu ON mu.id = cp.user_id "
            + "      WHERE cp.post_id=:postId LIMIT 2  ")
    List<PostCommentResponseDTO> findByPostId(@Param("postId") int postId);

    @Query("    SELECT  "
            + "      cp.*,  "
            + "      mu.username  "
            + "    FROM  "
            + "      comment_post cp  "
            + "      JOIN master_users mu ON mu.id = cp.user_id "
            + "      WHERE cp.post_id=:postId  AND cp.id >:id LIMIT 2  ")
    List<PostCommentResponseDTO> findByPostIdAndId(@Param("postId") int postId, @Param("id") int id);
}
