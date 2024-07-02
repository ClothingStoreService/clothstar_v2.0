package org.store.clothstar.common.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void redisTest() throws Exception {
        //given
        String email = "test@test.com";
        String code = "aaa111";

        //when
        redisUtil.setDataExpire(email, code);

        //then
        Assertions.assertTrue(redisUtil.existData("test@test.com"));
        Assertions.assertFalse(redisUtil.existData("test1@test.com"));
        Assertions.assertEquals(redisUtil.getData(email), "aaa111");
    }
}