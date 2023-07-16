package com.adn.cloneig.services;

import com.adn.cloneig.dto.ProfilePost;
import com.adn.cloneig.dto.ProfileResponseDTO;
import com.adn.cloneig.dto.RegisterRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppUserService {
    void registerUser(RegisterRequestDTO dto);

    ProfileResponseDTO getProfile(String username);

    void editProfile(String username, MultipartFile multipartFile) throws IOException;
}
