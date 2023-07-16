package com.adn.cloneig.services;

import com.adn.cloneig.dto.*;

import java.util.List;

public interface FollowerService {
    List<SuggestionsResponseDTO> getSuggestionsUser();

    public void followUser(FollowRequestDTO dto);
}
