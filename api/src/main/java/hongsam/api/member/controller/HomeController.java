package hongsam.api.member.controller;

import hongsam.api.jwt.TokenProvider;
import hongsam.api.member.domain.MemberDto;
import hongsam.api.member.domain.MemberResponse;
import hongsam.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping("/question/{questionId}")
    public MemberResponse getUUID(HttpServletRequest request, @PathVariable("questionId") String questionId) {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);

        return memberService.getUUID(memberDto.getEmail());

    }

    @PostMapping("/login-check")
    public MemberDto loginCheck(HttpServletRequest request) {
        return tokenProvider.getMemberByAccessToken(request);
    }

}
