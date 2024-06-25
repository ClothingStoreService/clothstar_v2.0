package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;

@Tag(name = "index", description = "회원가입, 로그인, 로그아웃 기능과 user, seller, admin 페이지로 이동하기 위한 API 입니다.")
@Controller
public class MemberViewController {
    @GetMapping("/main")
    public String main() {
        return "index";
    }

    @GetMapping("/membersPagingOffset")
    public String membersPagingOffset() {
        return "memberOffsetList";
    }

    @GetMapping("/membersPagingSlice")
    public String membersPagingSlice() {
        return "memberSliceList";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    @ResponseBody
    public MessageDTO userPage() {
        return MessageDTOBuilder.buildMessage(HttpStatus.OK.value(), "인증성공");
    }

    @GetMapping("/seller")
    @ResponseBody
    public MessageDTO sellerPage() {
        return MessageDTOBuilder.buildMessage(HttpStatus.OK.value(), "인증성공");
    }

    @GetMapping("/admin")
    @ResponseBody
    public MessageDTO adminPage() {
        return MessageDTOBuilder.buildMessage(HttpStatus.OK.value(), "인증성공");
    }
}