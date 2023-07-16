package com.adn.cloneig.services.impl;

import com.adn.cloneig.configs.AppProperties;
import com.adn.cloneig.dto.*;
import com.adn.cloneig.exceptions.ResourceNotFoundException;
import com.adn.cloneig.model.CommentPost;
import com.adn.cloneig.model.LikePost;
import com.adn.cloneig.model.Posts;
import com.adn.cloneig.repository.CommentPostRepo;
import com.adn.cloneig.repository.LikePostRepo;
import com.adn.cloneig.repository.PostQueryRepo;
import com.adn.cloneig.repository.PostsRepo;
import com.adn.cloneig.services.PostService;
import com.adn.cloneig.utils.FileUploadUtil;
import com.adn.cloneig.utils.UserAktif;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;


import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostsRepo postsRepo;
    private CommentPostRepo commentPostRepo;
    private LikePostRepo likePostRepo;

    private ModelMapper modelMapper;

    private AppProperties appProperties;
    private PostQueryRepo postQueryRepo;

    private UserAktif userAktif;

    @Override
    @Transactional
    public int addPost(PostRequestDTO dto, MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Posts post = new Posts();
        post.setCaption(dto.getCaption());
        post.setImage(fileName);
        post.setUserId(userAktif.getUserId());
        postsRepo.save(post);
        String uploadDir = "storage/uploads/";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return post.getId();
    }

    @Override
    @Transactional
    public int addComment(PostAddCommentRequestDTO dto) {
        Posts posts =  postsRepo.findAndLockById(dto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("post id not found"));
        CommentPost comment = new CommentPost();
        comment.setKomentar(dto.getKomentar());
        comment.setPostId(dto.getPostId());
        comment.setUserId(userAktif.getUserId());
        commentPostRepo.save(comment);
        posts.setJmlComment(posts.getJmlComment() + 1);
        postsRepo.save(posts);
        return posts.getId();
    }

    @Override
    @Transactional
    public void likePost(PostLikeRequestDTO dto) {
        Posts posts =  postsRepo.findAndLockById(dto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("post id not found"));
        LikePost likes = likePostRepo.findByUserIdAndPostId(userAktif.getUserId(),dto.getPostId());
        if(likes != null){
            likes.setState(dto.getState());
        }else{
            likes = new LikePost();
            likes.setPostId(dto.getPostId());
            likes.setState(dto.getState());
            likes.setUserId(userAktif.getUserId());
        }
        likePostRepo.save(likes);
        int jmlLike = dto.getState() ? 1 : -1;
        posts.setJmlLike(posts.getJmlLike()  + jmlLike);
        postsRepo.save(posts);

    }

    @Override
    public PageResponseDTO findPost(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo,limit);

//        Map<Integer,List<QueryPost>> data = postsRepo.findPostAndKomenByUserId(2, pageable.getPageSize(), pageable.getOffset()).stream().collect(Collectors.groupingBy(
//            QueryPost::getId,Collectors.toList()
//        ));
//
//        List<PostResponse> postResponses = data.values().stream().map((v)->{
//            PostResponse postResponse = new PostResponse();
//            postResponse.setId(v.get(0).getId());
//            postResponse.setImage(v.get(0).getImage());
//            postResponse.setCaption(v.get(0).getCaption());
//            postResponse.setJmlComment(v.get(0).getJmlComment());
//            postResponse.setJmlLike(v.get(0).getJmlLike());
//            postResponse.setUsername(v.get(0).getUsername());
//            postResponse.setLikeState(v.get(0).getLikeState());
//            postResponse.setCreatedAt(v.get(0).getCreatedAt());
//            List<PostCommentResponse> postCommentResponses = v.stream().map((e)->{
//                PostCommentResponse postCommentResponse = new PostCommentResponse();
//                postCommentResponse = modelMapper.map(e,PostCommentResponse.class);
//                postCommentResponse.setPostId(e.getId());
//                return postCommentResponse;
//            }).collect(Collectors.toList());
//            postResponse.setComments(postCommentResponses);
//            return postResponse;
//        }).collect(Collectors.toList());

        Page<PostResponseDTO> data = postQueryRepo.getPosts(userAktif.getUserId(), pageable);
        List<PostResponseDTO> postResponsDTOS = data.getContent().stream().map((e)->{
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            //postResponseDTO = modelMapper.map(e, PostResponseDTO.class);
            postResponseDTO.setId(e.getId());
            postResponseDTO.setUserId(e.getUserId());
            postResponseDTO.setCaption(e.getCaption());
            postResponseDTO.setImage(appProperties.getUploadStorage() + e.getImage());
            postResponseDTO.setJmlLike(e.getJmlLike());
            postResponseDTO.setJmlComment(e.getJmlComment());
            postResponseDTO.setCreatedAt(e.getCreatedAt());
            postResponseDTO.setUsername(e.getUsername());
            postResponseDTO.setLikeState(e.getLikeState());
            if(e.getImageProfile() != null) {
                postResponseDTO.setImageProfile(appProperties.getProfileStorage() + e.getImageProfile());
            }else{
                postResponseDTO.setImageProfile(appProperties.getImgProfileDefault());
            }
            postResponseDTO.setComments(commentPostRepo.findByPostId(e.getId()));
            return postResponseDTO;
        }).collect(Collectors.toList());


        PageResponseDTO pageResponseDTO = new PageResponseDTO();
        pageResponseDTO.setPageNo(pageNo);
        pageResponseDTO.setPageSize(data.getSize());
        pageResponseDTO.setContent(postResponsDTOS);
        pageResponseDTO.setTotalElements((int) data.getTotalElements());
        pageResponseDTO.setTotalPages(data.getTotalPages());
        return  pageResponseDTO;
    }

    @Override
    public List<PostCommentResponseDTO> getMoreComments(int postId, int commentId) {
        return commentPostRepo.findByPostIdAndId(postId,commentId);
    }
}
