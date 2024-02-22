package hongsam.api.member.controller;

import hongsam.api.jwt.TokenProvider;
import hongsam.api.member.domain.*;
import hongsam.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    @PostMapping("/signup")
    public MemberResponse signup(@RequestBody Member member) {
        return memberService.signup(member);
    }

    // 이메일 중복 체크
    // 문제점 : emailCheck 한 후 통과 후 다른 사용자가 동일 이메일 입력한 경우 (나중에 생각)
    @PostMapping("/signup/email-check")
    public MemberResponse emailCheck(@RequestBody EmailCheckDto emailCheckDto) {
        return memberService.emailCheck(emailCheckDto.getEmail());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(httpHeaders).body(accessToken);
    }

    @PostMapping("/test")
    public String test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    // 로그아웃
//    @PostMapping("/logout")
//    public MemberResponse logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        if (session == null || session.getAttribute("loginMember") == null) {
//            log.info("로그아웃 실패");
//            return new MemberResponse(400,"로그아웃 실패");
//        }
//        session.invalidate();
//        log.info("로그아웃 성공");
//        return new MemberResponse(200,"로그아웃 성공");
//    }
}
