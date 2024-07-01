package org.store.clothstar.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigurationUnitTest {
    @Autowired
    MockMvc mockMvc;

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
    void userPageAuthorityTest_anonymousUser() throws Exception {
        //given
        final String url = "/user";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isForbidden());
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
                .andExpect(status().isForbidden());
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
    void adminPageAuthorityTest_anonymousUser() throws Exception {
        //given
        final String url = "/admin";

        //when && then
        mockMvc
                .perform(get(url))
                .andExpect(status().isForbidden());
    }
}