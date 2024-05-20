package org.store.clothstar.order.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIBuilder {

    public static URI buildURI(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest() // 현재 요청의 URI를 사용
                .path("/{id}")  // 경로 변수 추가
                .buildAndExpand(id) // {/id} 자리에 실제 id 값을 삽입
                .toUri();
    }
}
