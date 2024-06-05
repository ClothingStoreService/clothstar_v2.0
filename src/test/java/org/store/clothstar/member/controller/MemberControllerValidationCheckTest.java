package org.store.clothstar.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyPasswordRequest;
import org.store.clothstar.member.repository.MemberJpaRepositoryAdapter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MemberControllerValidationCheckTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberJpaRepositoryAdapter memberJpaRepository;

    private static final String MEMBER_SIGN_UP_URL = "/v1/members";

    @DisplayName("회원가입시 이메일 양식을 지켜야 한다..")
    @Test
    void signUp_emailValidationTest() throws Exception {
        //given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("testcom")
                .password("1234567")
                .name("현수")
                .telNo("010-1234-1244")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        Assertions.assertThat(createMemberRequest.getPassword().length()).isLessThan(8);
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.email").value("유효하지 않은 이메일 형식입니다."));
    }

    @DisplayName("회원가입시 비밀번호는 최소 8자리 이상이여야 한다.")
    @Test
    void signUp_passwordValidationTest() throws Exception {
        //given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("test@test.com")
                .password("1234567")
                .name("현수")
                .telNo("010-1234-1244")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        Assertions.assertThat(createMemberRequest.getPassword().length()).isLessThan(8);
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.password").value("비밀번호는 최소 8자 이상이어야 합니다."));
    }

    @DisplayName("회원가입시 이름은 필수 값이다.")
    @Test
    void signUp_nameValidationTest() throws Exception {
        //given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("test@test.com")
                .password("1234567")
                .name("")
                .telNo("010-1234-1244")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        Assertions.assertThat(createMemberRequest.getPassword().length()).isLessThan(8);
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.name").value("이름은 비어 있을 수 없습니다."));
    }

    @DisplayName("회원가입시 전화번호 양식이 지켜져야 한다.")
    @Test
    void signUp_telNoValidationTest() throws Exception {
        //given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("test@test.com")
                .password("1234567")
                .name("현수")
                .telNo("1244")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        Assertions.assertThat(createMemberRequest.getPassword().length()).isLessThan(8);
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.telNo").value("유효하지 않은 전화번호 형식입니다."));
    }

    @DisplayName("비밀번호 변경 요청시에도 비밀번호는 8자리 이상이여야 한다.")
    @WithMockUser(username = "kang", roles = "USER")
    @Test
    void modifyPassword_validCheckTest() throws Exception {
        //given
        String modifyPasswordURL = "/v1/members/1";
        ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest("1");
        final String requestBody = objectMapper.writeValueAsString(modifyPasswordRequest);

        //when
        ResultActions actions = mockMvc.perform(patch(modifyPasswordURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.password").value("비밀번호는 최소 8자 이상이어야 합니다."));
    }
}
