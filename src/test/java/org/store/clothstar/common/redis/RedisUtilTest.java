package org.store.clothstar.common.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @DisplayName("redis 데이터가 생성되고 삭제되는지 확인한다.")
    @Test
    public void redisCreateAndDeleteTest() throws Exception {
        //redis 데이터가 생성됐는지 확인한다.
        //given
        String key = "test@test.com";
        String value = "aaa111";

        //when
        redisUtil.setDataExpire(key, value);

        //then
        Assertions.assertTrue(redisUtil.existData(key));
        Assertions.assertEquals(redisUtil.getData(key), value);

        //redis 데이터가 삭제 됐는지 확인한다.
        //when
        redisUtil.deleteData(key);

        //then
        Assertions.assertFalse(redisUtil.existData(key));
    }
}