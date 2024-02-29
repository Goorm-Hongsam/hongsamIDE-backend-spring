package hongsam.api.member.controller;

import hongsam.api.jwt.TokenProvider;
import hongsam.api.member.domain.*;
import hongsam.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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
    public ResponseEntity<String> signup(@RequestBody Member member) {
        return memberService.signup(member);
    }

    // 이메일 중복 체크
    // 문제점 : emailCheck 한 후 통과 후 다른 사용자가 동일 이메일 입력한 경우 (나중에 생각)
    @PostMapping("/signup/email-check")
    public ResponseEntity<String> emailCheck(@RequestBody EmailCheckDto emailCheckDto) {
        return memberService.emailCheck(emailCheckDto.getEmail());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(@RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(httpHeaders)
                .body(new MemberDto(userDetails.getUsername(), userDetails.getUserNickname(), userDetails.getUuid(), userDetails.getProfileUrl(), authorities));
    }

    @PostMapping("/login-check")
    public MemberDto loginCheck(HttpServletRequest request) {
        return tokenProvider.getMemberByAccessToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("토큰 정보를 지워주세요.", HttpStatus.NOT_ACCEPTABLE);
    }

//    @PostMapping("/test")
//    public UserDetails test() {
//
//        // 현재 프로젝트에 인증/인가 모두 구현했을 경우
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        return userDetails;
//    }

    // jwt에서 정보 꺼내기 테스트
    @PostMapping("/test2")
    public MemberDto test(HttpServletRequest request) {

        return tokenProvider.getMemberByAccessToken(request);
    }

}
