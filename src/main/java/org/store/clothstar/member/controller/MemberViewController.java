package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "index", description = "회원가입, 로그인, 로그아웃 기능과 user, seller, admin 페이지로 이동하기 위한 API 입니다.")
@Controller
public class MemberViewController {
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
    public String userPage() {
        return "인증성공";
    }

    @GetMapping("/userPage")
    public String viewUserPage() {
        return "/userPage";
    }

    @GetMapping("/seller")
    @ResponseBody
    public String sellerPage() {
        return "인증성공";
    }

    @GetMapping("/sellerPage")
    public String viewSellerPage() {
        return "/sellerPage";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String adminPage() {
        return "인증성공";
    }

    @GetMapping("/adminPage")
    public String viewAdminPage() {
        return "/adminPage";
    }
}