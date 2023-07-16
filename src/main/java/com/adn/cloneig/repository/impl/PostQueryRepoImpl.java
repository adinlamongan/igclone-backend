package com.adn.cloneig.repository.impl;

import com.adn.cloneig.dto.PostResponseDTO;
import com.adn.cloneig.repository.PostQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostQueryRepoImpl implements PostQueryRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int  count(int userId){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);

        String sql = "SELECT SUM(jumlah) AS total_element FROM ("
                + "SELECT  "
                + "  count(p.id) as jumlah  "
                + "FROM  "
                + "  posts p  "
                + "  JOIN master_users mu ON mu.id = p.user_id  "
                + "  LEFT JOIN like_post lp ON (lp.user_id = p.user_id AND lp.post_id = p.id)  "
                + "WHERE  "
                + "  p.user_id =:userId  "
                + "UNION ALL  "
                + "SELECT  "
                + "  count(p.id) as jumlah  "
                + "FROM  "
                + "  posts p  "
                + "  JOIN master_users mu ON mu.id = p.user_id  "
                + "  JOIN followers f ON f.follower_user_id = p.user_id  "
                + "  LEFT JOIN like_post lp ON (lp.user_id = p.user_id AND lp.post_id = p.id)  "
                + "WHERE  "
                + "  f.follower_user_id =:userId  "
                + ") as v";
        return  namedParameterJdbcTemplate.queryForObject(sql,parameters, Integer.class);

    }

    @Override
    public Page<PostResponseDTO> getPosts(int userId, Pageable page) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        String sql ="SELECT  "
                + "  p.id,  "
                + "  p.user_id,  "
                + "  mu.username,  "
                + "  p.caption,  "
                + "  p.image,  "
                + "  p.jml_like,  "
                + "  p.jml_comment,  "
                + "  p.created_at,  "
                + "  mu.image as image_profile,  "
                + "  COALESCE(lp.state, lp.state, false) AS like_state  "
                + "FROM  "
                + "  posts p  "
                + "  JOIN master_users mu ON mu.id = p.user_id  "
                + "  LEFT JOIN like_post lp ON (lp.user_id = p.user_id AND lp.post_id = p.id)  "
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
                + "  mu.image as image_profile,  "
                + "  COALESCE(lp.state, lp.state, false) AS like_state  "
                + "FROM  "
                + "  posts p  "
                + "  JOIN master_users mu ON mu.id = p.user_id  "
                + "  JOIN followers f ON f.follower_user_id = p.user_id  "
                + "  LEFT JOIN like_post lp ON (lp.user_id = p.user_id AND lp.post_id = p.id)  "
                + "WHERE  "
                + "  f.follower_user_id =:userId  "
                + "ORDER BY  "
                + "  created_at desc  "
                + "LIMIT "+ page.getPageSize() + " OFFSET " + page.getOffset();
        List<PostResponseDTO> listPost = namedParameterJdbcTemplate.query(sql, parameters, new PostResponesRowMapper());
        return new PageImpl<>(listPost, page, count(userId) );
    }

    private static class PostResponesRowMapper implements RowMapper<PostResponseDTO> {

        @Override
        public PostResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(rs.getInt("id"));
            dto.setUserId(rs.getInt("user_id"));
            dto.setCaption(rs.getString("caption"));
            dto.setImage(rs.getString("image"));
            dto.setJmlLike(rs.getInt("jml_like"));
            dto.setJmlComment(rs.getInt("jml_comment"));
            dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            dto.setImageProfile(rs.getString("image_profile"));
            dto.setUsername(rs.getString("username"));
            dto.setLikeState(rs.getBoolean("like_state"));
            dto.setImageProfile(rs.getString("image_profile"));
            return dto;
        }
    }
}
