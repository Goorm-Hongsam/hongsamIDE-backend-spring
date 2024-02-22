package hongsam.api.member.controller;

import hongsam.api.member.domain.MemberResponse;
import hongsam.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("api/question/{questionId}")
    public MemberResponse getUUID(HttpServletRequest request, @PathVariable("questionId") String questionId) {


//        return memberService.getUUID(loginMemberResponse.getEmail());
        return null;

    }

}
