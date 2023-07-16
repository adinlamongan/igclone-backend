package com.adn.cloneig.services.impl;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.ProfilePost;
import com.adn.cloneig.dto.ProfileResponseDTO;
import com.adn.cloneig.dto.RegisterRequestDTO;
import com.adn.cloneig.exceptions.ResourceNotFoundException;
import com.adn.cloneig.model.AppUser;
import com.adn.cloneig.model.Posts;
import com.adn.cloneig.repository.AppUserRepo;
import com.adn.cloneig.repository.PostsRepo;
import com.adn.cloneig.services.AppUserService;
import com.adn.cloneig.utils.FileUploadUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;
    private PostsRepo postsRepo;
    private AppProperties appProperties;


    @Override
    public void registerUser(RegisterRequestDTO dto) {
        AppUser appUser = new AppUser();
        appUser.setEmail(dto.getEmail());
        appUser.setUsername(dto.getUsername());
        appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        appUserRepo.save(appUser);
    }

    @Override
    public ProfileResponseDTO getProfile(String username) {
        AppUser appUser = appUserRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("username not found"));
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(appUser.getId());
        String image = appUser.getImage() == null ? appProperties.getImgProfileDefault()
                : appProperties.getProfileStorage() + appUser.getImage();
        dto.setImage(image);
        dto.setEmail(appUser.getEmail());
        dto.setUsername(appUser.getUsername());
        dto.setJmlMengikuti(appUser.getJmlMengikuti());
        dto.setJmlMengikuti(appUser.getJmlMengikuti());
        List<ProfilePost> profilePosts = postsRepo.findByUserId(appUser.getId()).stream().map((e) -> {
            ProfilePost profilePost = new ProfilePost();
            profilePost.setId(e.getId());
            profilePost.setImage(appProperties.getUploadStorage() + e.getImage());
            return  profilePost;
        }).collect(Collectors.toList());
        dto.setPosts(profilePosts);
        return dto;
    }

    @Override
    public void editProfile(String username, MultipartFile multipartFile) throws IOException {
        AppUser appUser = appUserRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("username not found"));
        String uploadDir = "storage/profile/";
        String fileName = appUser.getUsername() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());// StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename().));
        //StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        appUser.setImage(fileName);
        appUserRepo.save(appUser);
    }
}
