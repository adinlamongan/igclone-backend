package com.adn.cloneig.repository;

import com.adn.cloneig.model.AppUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepo extends CrudRepository<AppUser, Integer> {

    @Query("SELECT * FROM master_users where id=:id for update")
    Optional<AppUser> findAndLockById(@Param("id") int id);

    Boolean existsByEmail(String email);    
    Boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

}
