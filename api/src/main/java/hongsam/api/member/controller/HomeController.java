package hongsam.api.member.controller;

import hongsam.api.jwt.TokenProvider;
import hongsam.api.member.domain.MemberDto;
import hongsam.api.member.domain.MemberResponse;
import hongsam.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping("/api/question/{questionId}")
    public MemberResponse getUUID(HttpServletRequest request, @PathVariable("questionId") String questionId) {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);

        return memberService.getUUID(memberDto.getEmail());

    }

}
