package com.adn.cloneig.repository;

import com.adn.cloneig.model.Posts;
import com.adn.cloneig.model.PostQuery;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostsRepo extends CrudRepository<Posts, Integer> {

    @Query("select * from posts where id=:id for update")
    Optional<Posts> findAndLockById(@Param("id") int id);

    @Query("SELECT  "
            + "  p.id,  "
            + "  p.user_id,  "
            + "  mu.username,  "
            + "  p.caption,  "
            + "  p.image,  "
            + "  p.jml_like,  "
            + "  p.jml_comment,  "
            + "  p.created_at,  "
            + "  COALESCE(lp.state, lp.state, false) AS like_state,  "
            + "  v.komentar,  "
            + "  COALESCE(v.user_id, v.user_id, 0) AS user_id_komen,  "
            + "  v.username AS username_komen,  "
            + "  v.created_at AS created_at_komen  "
            + "FROM  "
            + "  posts p  "
            + "  JOIN master_users mu ON mu.id = p.user_id  "
            + "  LEFT JOIN like_post lp ON lp.user_id = p.user_id  "
            + "  AND lp.post_id = p.id  "
            + "  LEFT JOIN LATERAL ( "
            + "    SELECT  "
            + "      cp.*,  "
            + "      mu.username  "
            + "    FROM  "
            + "      comment_post cp  "
            + "      JOIN master_users mu ON mu.id = cp.user_id "
            + "      WHERE cp.post_id=p.id LIMIT 3  "
            + "  ) AS v ON TRUE "
            + "WHERE  "
            + "  p.user_id =:userId  "
            + "UNION ALL  "
            + "SELECT  "
            + "  p.id,  "
            + "  p.user_id,  "
            + "  mu.username,  "
            + "  p.caption,  "
            + "  p.image,  "
            + "  p.jml_like,  "
            + "  p.jml_comment,  "
            + "  p.created_at,  "
            + "  COALESCE(lp.state, lp.state, false) AS like_state,  "
            + "  v.komentar,  "
            + "  COALESCE(v.user_id, v.user_id, 0) AS user_id_komen,  "
            + "  v.username AS username_komen,  "
            + "  v.created_at AS created_at_komen  "
            + "FROM  "
            + "  posts p  "
            + "  JOIN master_users mu ON mu.id = p.user_id  "
            + "  JOIN followers f ON f.follower_user_id = p.user_id  "
            + "  LEFT JOIN like_post lp ON lp.user_id = p.user_id  "
            + "  AND lp.post_id = p.id  "
            + "  LEFT JOIN LATERAL ( "
            + "    SELECT  "
            + "      cp.*,  "
            + "      mu.username  "
            + "    FROM  "
            + "      comment_post cp  "
            + "      JOIN master_users mu ON mu.id = cp.user_id "
            + "      WHERE cp.post_id=p.id LIMIT 3  "
            + "  ) AS v ON TRUE "
            + "WHERE  "
            + "  f.follower_user_id =:userId  "
            + "ORDER BY  "
            + "  created_at desc  "
            + "LIMIT :pageSize OFFSET :pageOffset "
            )
    List<PostQuery> findPostAndKomenByUserId(@Param("userId") int userId, @Param("pageSize") int pageSize, @Param("pageOffset") Long pageOffset);

    @Query("SELECT * FROM posts where user_id=:userId ORDER BY id DESC LIMIT 10")
    List<Posts> findByUserId(@Param("userId") int userId);


}
