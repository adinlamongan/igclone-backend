package com.adn.cloneig.services.impl;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.*;
import com.adn.cloneig.model.AppUser;
import com.adn.cloneig.model.Followers;
import com.adn.cloneig.model.LikePost;
import com.adn.cloneig.model.Posts;
import com.adn.cloneig.repository.*;
import com.adn.cloneig.utils.FileUploadUtil;
import com.adn.cloneig.utils.UserAktif;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostsRepo postsRepo;
    @Mock
    private PostQueryRepo postQueryRepo;
    @Mock
    private CommentPostRepo commentPostRepo;
    @Mock
    private LikePostRepo likePostRepo;
    @Mock
    private AppProperties appProperties;

    @Mock
    private UserAktif userAktif;

    private FileUploadUtil fileUploadUtil;


    @InjectMocks
    private PostServiceImpl postService;
    private int loginUserId;

    private Posts posts;

    private LikePost likePost;

    private PostLikeRequestDTO dto;

    private String baseUrlStorage = "http://127.0.0.1:8080/storage/";
    private String profileStorage = "profile/";
    private String imgProfileDefault = "img/default.png";

    @BeforeEach
    public void init() {
        loginUserId = 3;

        posts = new Posts();
        posts.setId(1);
        posts.setJmlLike(7);

        likePost = new LikePost();
        likePost.setPostId(posts.getId());
        likePost.setState(true);
        likePost.setUserId(3);

        dto = new PostLikeRequestDTO();
        dto.setPostId(posts.getId());
        when(userAktif.getUserId()).thenReturn(loginUserId);
    }


    @Test
    void addPost() throws IOException {
        PostRequestDTO dto = new PostRequestDTO();
        dto.setCaption("ini caption testing");
        Posts post = new Posts();
        post.setCaption(dto.getCaption());
        //post.setImage(fileName);
        post.setUserId(userAktif.getUserId());
        MultipartFile multipartFile = new MockMultipartFile("image", "", "application/json", "{\"image\": \"C:\\Users\\Hp\\Desktop\\Screenshot_1.png\"}".getBytes());
        int coba = postService.addPost(dto,multipartFile);
        verify(postsRepo,times(1)).save(Mockito.any(Posts.class));
//        try (MockedStatic<FileUploadUtil> utilities = Mockito.mockStatic(FileUploadUtil.class)) {
//            String uploadDir = "storage/uploads/";
//            utilities.when(() -> FileUploadUtil.saveFile(uploadDir,post.getImage(),multipartFile))
//                    .thenReturn();
//
//            assertThat(StaticUtils.range(2, 6)).containsExactly(10, 11, 12);
//        }
        Assertions.assertNotNull(coba);


    }

    @Test
    void likePost() {

        dto.setState(true);

        when(postsRepo.findAndLockById(posts.getId())).thenReturn(Optional.of(posts));
        when(likePostRepo.findByUserIdAndPostId(loginUserId,posts.getId())).thenReturn(likePost);
        postService.likePost(dto);
        verify(postsRepo,times(1)).findAndLockById(posts.getId());
        verify(likePostRepo,times(1)).findByUserIdAndPostId(loginUserId, posts.getId());
        verify(likePostRepo,times(1)).save(likePost);
        verify(postsRepo,times(1)).save(posts);
        Assertions.assertEquals(8, posts.getJmlLike());
    }

    @Test
    void likePostFalse() {

        dto.setState(false);

        when(postsRepo.findAndLockById(posts.getId())).thenReturn(Optional.of(posts));
        when(likePostRepo.findByUserIdAndPostId(loginUserId,posts.getId())).thenReturn(likePost);
        postService.likePost(dto);
        verify(postsRepo,times(1)).findAndLockById(posts.getId());
        verify(likePostRepo,times(1)).findByUserIdAndPostId(loginUserId, posts.getId());
        verify(likePostRepo,times(1)).save(likePost);
        verify(postsRepo,times(1)).save(posts);
        Assertions.assertEquals(6, posts.getJmlLike());
    }

    @Test
    void findPost() {

        List<PostResponseDTO> list = new ArrayList<>();
        list.add(new PostResponseDTO(1, 11, "caption 1", "image 1", 21, 31, LocalDateTime.now().withNano(0), "username 1", true, "ip 1", null));
        list.add(new PostResponseDTO(2, 12, "caption 2", "image 2", 22, 32, LocalDateTime.now().withNano(0), "username 2", true, null, null));
        list.add(new PostResponseDTO(3, 13, "caption 3", "image 3", 23, 33, LocalDateTime.now().withNano(0), "username 3", true, "ip 3", null));

        List<PostCommentResponseDTO> listKomen = new ArrayList<>();
        listKomen.add(new PostCommentResponseDTO(1,1,1,"ini komen 1",LocalDateTime.now().withNano(0),"uk1"));
        listKomen.add(new PostCommentResponseDTO(2,2,1,"ini komen 2",LocalDateTime.now().withNano(0),"uk2"));
        listKomen.add(new PostCommentResponseDTO(3,3,1,"ini komen 3",LocalDateTime.now().withNano(0),"uk3"));
        List<PostCommentResponseDTO> listKomen2 = new ArrayList<>();
        List<PostCommentResponseDTO> listKomen3 = new ArrayList<>();
        listKomen.add(new PostCommentResponseDTO(1,1,3,"ini komen 1",LocalDateTime.now().withNano(0),"uk1"));
        listKomen.add(new PostCommentResponseDTO(2,2,3,"ini komen 2",LocalDateTime.now().withNano(0),"uk2"));
        listKomen.add(new PostCommentResponseDTO(3,3,3,"ini komen 3",LocalDateTime.now().withNano(0),"uk3"));

        Page<PostResponseDTO> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0,3);

        when(appProperties.getProfileStorage()).thenReturn(baseUrlStorage + profileStorage);
        when(appProperties.getImgProfileDefault()).thenReturn(baseUrlStorage + imgProfileDefault);
        when(postQueryRepo.getPosts(loginUserId,pageable)).thenReturn(page);
        when(commentPostRepo.findByPostId(1)).thenReturn(listKomen);
        when(commentPostRepo.findByPostId(2)).thenReturn(listKomen2);
        when(commentPostRepo.findByPostId(3)).thenReturn(listKomen3);
        List<PostResponseDTO> postResponsDTOS = page.getContent().stream().map((e)->{

            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(e.getId());
            postResponseDTO.setUserId(e.getUserId());
            postResponseDTO.setCaption(e.getCaption());
            postResponseDTO.setImage("http://127.0.0.1:8080/storage/uploads/" + e.getImage());
            postResponseDTO.setJmlLike(e.getJmlLike());
            postResponseDTO.setJmlComment(e.getJmlComment());
            postResponseDTO.setCreatedAt(e.getCreatedAt());
            postResponseDTO.setUsername(e.getUsername());
            postResponseDTO.setLikeState(e.getLikeState());
            if(e.getImageProfile() != null) {
                postResponseDTO.setImageProfile(baseUrlStorage + profileStorage + e.getImageProfile());
            }else{
                postResponseDTO.setImageProfile(baseUrlStorage + imgProfileDefault);
            }
            if (e.getId() == 1) {
                postResponseDTO.setComments(listKomen);
            } else if (e.getId()==2) {
                postResponseDTO.setComments(listKomen2);
            } else if (e.getId()==3) {
                postResponseDTO.setComments(listKomen3);
            }
            return postResponseDTO;
        }).collect(Collectors.toList());
        PageResponseDTO pageResponseDTO = new PageResponseDTO();
        pageResponseDTO.setPageNo(0);
        pageResponseDTO.setPageSize(page.getSize());
        pageResponseDTO.setContent(postResponsDTOS);
        pageResponseDTO.setTotalElements(3);
        pageResponseDTO.setTotalPages(1);

        postService.findPost(0,3);

    }
}