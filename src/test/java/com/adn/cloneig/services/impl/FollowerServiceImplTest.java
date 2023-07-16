package com.adn.cloneig.services.impl;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.FollowRequestDTO;
import com.adn.cloneig.dto.SuggestionsResponseDTO;
import com.adn.cloneig.exceptions.ResourceNotFoundException;
import com.adn.cloneig.model.AppUser;
import com.adn.cloneig.model.Followers;
import com.adn.cloneig.repository.AppUserRepo;
import com.adn.cloneig.repository.FollowerRepo;
import com.adn.cloneig.utils.UserAktif;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerServiceImplTest {

    @Mock
    private FollowerRepo followerRepo;

    @Mock
    private AppProperties appProperties;


    @Mock
    private AppUserRepo appUserRepo;
    @Mock
    private UserAktif userAktif;

    @InjectMocks
    private FollowerServiceImpl followerService;

    private AppUser appUserLogin;
    private AppUser appUser;
    private FollowRequestDTO followRequestDTO;
    private Followers followers;
    private int loginUserId;
    private String baseUrlStorage = "http://127.0.0.1:8080/storage/";
    private String profileStorage = "profile/";
    private String imgProfileDefault = "img/default.png";


    @BeforeEach
    public void init() {

        loginUserId = 3;
        appUserLogin = new AppUser();
        appUserLogin.setId(loginUserId);
        appUserLogin.setUsername("admin");
        appUserLogin.setJmlMengikuti(5);

        followRequestDTO = new FollowRequestDTO();
        followRequestDTO.setUserId(7);
        followRequestDTO.setValue(true);
        followers = new Followers();
        followers.setFollowerUserId(appUserLogin.getId());
        followers.setUserId(followRequestDTO.getUserId());
        followers.setCreatedAt(LocalDateTime.now().withNano(0));
        appUser = new AppUser();
        appUser.setId(followRequestDTO.getUserId());
        appUser.setUsername("User di follow");
        appUser.setJmlPengikut(13);


    }


    @Test
    void getSuggestionsUser() {
        List<SuggestionsResponseDTO> list = new ArrayList<>();
        list.add( new SuggestionsResponseDTO(1,"user 1","image profile 1", "username 1"));
        list.add( new SuggestionsResponseDTO(2,"user 2",null, "username 2"));
        list.add( new SuggestionsResponseDTO(3,"user 3","image profile 3", "username 3"));
        when(userAktif.getUserId()).thenReturn(loginUserId);
        when(appProperties.getProfileStorage()).thenReturn(baseUrlStorage + profileStorage);
        when(appProperties.getImgProfileDefault()).thenReturn(baseUrlStorage + imgProfileDefault);
        when(followerRepo.findSuggestionUser(loginUserId)).thenReturn(list);
        followerService.getSuggestionsUser();
        List<SuggestionsResponseDTO> listResult = followerService.getSuggestionsUser();
        List<SuggestionsResponseDTO> listExpect = list.stream().map(e -> {
            SuggestionsResponseDTO dto = new SuggestionsResponseDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setUsername(e.getUsername());
            if (e.getImageProfile() != null) {
                dto.setImageProfile(baseUrlStorage + profileStorage + e.getImageProfile());
            } else {
                dto.setImageProfile(baseUrlStorage + imgProfileDefault);
            }
            return dto;
        }).collect(Collectors.toList());
        Assertions.assertEquals(listExpect,listResult);
    }

    @Test
    void followUser() {

        when(userAktif.getUserId()).thenReturn(loginUserId);
        when(appUserRepo.findAndLockById(loginUserId)).thenReturn(Optional.of(appUserLogin));
        when(appUserRepo.findAndLockById(followRequestDTO.getUserId())).thenReturn(Optional.of(appUser));
        when(appUserRepo.save(appUserLogin)).thenReturn(appUserLogin);
        when(appUserRepo.save(appUser)).thenReturn(appUser);
        followerService.followUser(followRequestDTO);
        verify(appUserRepo,times(1)).findAndLockById(loginUserId);
        Assertions.assertEquals(3, followers.getFollowerUserId());
        Assertions.assertEquals(7, followers.getUserId());
        Assertions.assertEquals(6, appUserLogin.getJmlMengikuti());
        Assertions.assertEquals(14, appUser.getJmlPengikut());
        verify(followerRepo,times(1)).save(followers);
        verify(appUserRepo,times(1)).save(appUserLogin);
        verify(appUserRepo,times(1)).findAndLockById(followRequestDTO.getUserId());
        verify(appUserRepo,times(1)).save(appUser);

    }

    @Test
    void unFollowUser() {

        followRequestDTO.setValue(false);

        when(userAktif.getUserId()).thenReturn(loginUserId);
        when(appUserRepo.findAndLockById(loginUserId)).thenReturn(Optional.of(appUserLogin));
        when(appUserRepo.findAndLockById(followRequestDTO.getUserId())).thenReturn(Optional.of(appUser));
        when(appUserRepo.save(appUserLogin)).thenReturn(appUserLogin);
        followerService.followUser(followRequestDTO);
        Assertions.assertEquals(4, appUserLogin.getJmlMengikuti());
        Assertions.assertEquals(12, appUser.getJmlPengikut());
        verify(appUserRepo,times(1)).save(appUserLogin);
        verify(followerRepo,times(1)).deleteByUserIdAndUserIdFollowers(followRequestDTO.getUserId(), userAktif.getUserId());
        verify(appUserRepo,times(1)).findAndLockById(followRequestDTO.getUserId());
        verify(appUserRepo,times(1)).save(appUser);
    }

    @Test
    void FollowUserNotFound() {

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            followerService.followUser(followRequestDTO);

        });

    }
}