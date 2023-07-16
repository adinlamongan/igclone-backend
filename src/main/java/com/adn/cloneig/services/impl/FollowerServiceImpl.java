package com.adn.cloneig.services.impl;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.FollowRequestDTO;
import com.adn.cloneig.dto.SuggestionsResponseDTO;
import com.adn.cloneig.exceptions.ResourceNotFoundException;
import com.adn.cloneig.model.AppUser;
import com.adn.cloneig.model.Followers;
import com.adn.cloneig.repository.AppUserRepo;
import com.adn.cloneig.repository.FollowerRepo;
import com.adn.cloneig.services.FollowerService;
import com.adn.cloneig.utils.UserAktif;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private AppProperties appProperties;
    private FollowerRepo followerRepo;
    private ModelMapper modelMapper;
    private AppUserRepo appUserRepo;
    private UserAktif userAktif;

    @Override
    public List<SuggestionsResponseDTO> getSuggestionsUser() {
        List<SuggestionsResponseDTO> data = followerRepo.findSuggestionUser(userAktif.getUserId());
        return data.stream().map(e -> {
            SuggestionsResponseDTO dto = new SuggestionsResponseDTO();
            //dto = modelMapper.map(e, SuggestionsResponseDTO.class);
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setUsername(e.getUsername());
            if (e.getImageProfile() != null) {
                dto.setImageProfile(appProperties.getProfileStorage() + e.getImageProfile());
            } else {
                dto.setImageProfile(appProperties.getImgProfileDefault());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void followUser(FollowRequestDTO dto) {
        int userId = userAktif.getUserId();
        AppUser appUser = appUserRepo.findAndLockById(userId).orElseThrow(() -> new ResourceNotFoundException("User tidak di temukan"));
        if (dto.getValue()){
            Followers followers = new Followers();
            followers.setUserId(dto.getUserId());
            followers.setFollowerUserId(userId);
            followerRepo.save(followers);
            appUser.setJmlMengikuti(appUser.getJmlMengikuti() + 1);
        }else{
            followerRepo.deleteByUserIdAndUserIdFollowers(dto.getUserId(), userId);
            appUser.setJmlMengikuti(appUser.getJmlMengikuti() - 1);
        }
        appUserRepo.save(appUser);
        appUser = appUserRepo.findAndLockById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User sudah tidak ada"));
        if (dto.getValue()){
            appUser.setJmlPengikut(appUser.getJmlPengikut() + 1);
        }else{
            appUser.setJmlPengikut(appUser.getJmlPengikut() - 1);
        }
        appUserRepo.save(appUser);
    }
}
