package com.adn.cloneig.repository;

import com.adn.cloneig.dto.SuggestionsResponseDTO;
import com.adn.cloneig.model.Followers;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowerRepo extends CrudRepository<Followers, Integer> {


    @Query("SELECT "
            + "     u.id, "
            + "     u.name, "
            + "     u.image as image_profile, "
            + "     u.username "
            + " FROM "
            + "     master_users u "
            + " WHERE "
            + "     u.id NOT IN ( "
            + "     SELECT "
            + "         user_id "
            + "     FROM "
            + "         followers "
            + "     WHERE "
            + "         follower_user_id =:userId "
            + "     ) "
            + "     AND u.id !=:userId "
            + " ORDER BY "
            + "     id DESC "
            + " LIMIT  3")
    List<SuggestionsResponseDTO> findSuggestionUser(@Param("userId") int userId);

    @Modifying
    @Query("DELETE from followers where user_id=:userId AND follower_user_id=:followerUserId")
    void deleteByUserIdAndUserIdFollowers(@Param("userId") int userId, @Param("followerUserId") int followerUserId);
}
