package org.store.clothstar.common.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigurationUnitTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("인덱스 페이지는 권한 없이 사용이 가능한지 테스트")
    @Test
    void indexPageAuthorityTest() throws Exception {
        //given
        final String url = "/";

        //when && then
        mockMvc.perform(get(url).with(anonymous()))
                .andExpect(status().isOk());
    }

    @DisplayName("인증된 사용자는 User 페이지 사용이 가능한지 테스트")
    @Test
    @WithMockUser(username = "현수", roles = "USER")
    void userPageAuthorityTest_authenticatedUser() throws Exception {
        //given
        final String url = "/user";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk());
    }

    @DisplayName("비인증 사용자는 User 페이지 사용이 불가능한지 테스트")
    @Test
    @WithAnonymousUser
    void userPageAuthorityTest_anonymousUser() throws Exception {
        //given
        final String url = "/user";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("판매자는 판매자 페이지 사용이 가능한지 테스트")
    @Test
    @WithMockUser(roles = "SELLER")
    void sellerPageAuthorityTest_sellerUser() throws Exception {
        //given
        final String url = "/seller";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk());
    }

    @DisplayName("비인증 사용자는 판매자 페이지 사용이 불가능한지 테스트")
    @Test
    @WithAnonymousUser
    void sellerPageAuthorityTest_anonymousUser() throws Exception {
        //given
        final String url = "/seller";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Admin 권한 사용자는 admin 페이지 사용이 가능한지 테스트")
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPageAuthorityTest_adminUser() throws Exception {
        //given
        final String url = "/admin";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk());
    }

    @DisplayName("비인증 사용자는 admin 페이지 사용이 불가능 한지 테스트")
    @Test
    @WithAnonymousUser
    void adminPageAuthorityTest_anonymousUser() throws Exception {
        //given
        final String url = "/admin";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isUnauthorized());
    }
}