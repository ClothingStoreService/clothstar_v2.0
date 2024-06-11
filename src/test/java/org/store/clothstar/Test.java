package org.store.clothstar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {

    @DisplayName("")
    @org.junit.jupiter.api.Test
    void test() {
        Assertions.assertThat(true).isFalse();
    }
}
