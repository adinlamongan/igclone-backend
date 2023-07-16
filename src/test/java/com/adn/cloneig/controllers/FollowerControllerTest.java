package com.adn.cloneig.controllers;

import com.adn.cloneig.dto.FollowRequestDTO;
import com.adn.cloneig.dto.SuggestionsResponseDTO;
import com.adn.cloneig.services.FollowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FollowerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class FollowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowerService followerService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void getSuggestionUser() throws Exception {
        List<SuggestionsResponseDTO> list = new ArrayList<>();
        list.add( new SuggestionsResponseDTO(1,"user 1","image profile 1", "username 1"));
        list.add( new SuggestionsResponseDTO(2,"user 2","image profile 2", "username 2"));
        list.add( new SuggestionsResponseDTO(3,"user 3","image profile 3", "username 3"));
        when(followerService.getSuggestionsUser()).thenReturn(list);

        mockMvc.perform(get("/api/follow/suggestion"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].name", is("user 1")))
                .andExpect(jsonPath("$[1].name", is("user 2")))
                .andExpect(jsonPath("$[2].name", is("user 3")))
                .andDo(print());
    }

    @Test
    void followUser() throws Exception {

        FollowRequestDTO dto = new FollowRequestDTO();
        dto.setUserId(7);
        dto.setValue(true);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/follow").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
        ;

        Mockito.verify(followerService, times(1)).followUser(dto);
    }
}