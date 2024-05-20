package org.store.clothstar.productLine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ProductLineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final Long productId = 3L;

//    @DisplayName("상품 상세 조회 테스트")
//    @Test
//    void givenProducts_whenGetProducsList_thenGetProductsWhereDeletedAtIsNull() throws Exception {
//        // given
//        final String url = "/v1/products/" + productId;
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
//                .accept(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("내셔널지오그래픽 곰돌이 후드?????????티"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.brandName").value("내셔널지오그래픽키즈 제주점"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("많이 사주세용~"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(69000))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.totalStock").value(0))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.saleCount").value(0))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.productLineStatus").value("COMING_SOON"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.biz_no").value("232-05-02861"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value("2024-04-04T23:07:30"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt").value(Matchers.nullValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.deletedAt").value(Matchers.nullValue()));
//    }
}
