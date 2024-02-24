package hongsam.api.member.controller;

import hongsam.api.jwt.TokenProvider;
import hongsam.api.member.domain.*;
import hongsam.api.member.service.MemberService;
import hongsam.api.member.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/mypage")
public class MemberInfoController {

    private final S3Service s3Service;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    // 프로필 사진 수정
    @PostMapping("/profile-img")
    public ResponseEntity<MemberDto> updateProfileImg(HttpServletRequest request, @RequestParam("profileImg") MultipartFile multipartFile) throws IOException {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);

//        s3에 파일 올리기
        String imgUrl = s3Service.uploadFiles(memberDto.getUuid(), multipartFile, "profileImage");

        memberDto.setProfileUrl(imgUrl);

        // 토큰 재발급하기
        String accessToken = tokenProvider.updateAccessToken(memberDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(httpHeaders).body(memberDto);
    }

    // 회원 정보 수정
    @PutMapping("/info")
    public ResponseEntity<MemberResponse> updateMemberInfo(HttpServletRequest request, @RequestBody MemberUpdateDto memberUpdateDto) {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);

        MemberResponse memberResponse = memberService.updateMemberInfo(memberDto, memberUpdateDto.getUsername(), memberUpdateDto.getPassword());

        if (memberResponse.getStatus() == 200) { // 토큰 재발급
            String accessToken = tokenProvider.updateAccessToken((MemberDto) memberResponse.getData());
            HttpHeaders httpHeaders = new HttpHeaders();
            // response header에 jwt token에 넣어줌
            httpHeaders.add("Authorization", "Bearer " + accessToken);

            return ResponseEntity.ok().headers(httpHeaders).body(memberResponse);
        } else {
            return ResponseEntity.ok().body(memberResponse);
        }
    }

    // 비밀번호 확인
    @PostMapping("/pw-check")
    public MemberResponse checkPassword(HttpServletRequest request, @RequestBody PasswordCheckDto passwordCheckDto) {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);
        return memberService.checkPassword(passwordCheckDto.getPassword(), memberDto.getEmail());
    }

    // 회원 탙퇴
    @DeleteMapping("/members")
    public MemberResponse deleteMember(HttpServletRequest request) {

        MemberDto memberDto = tokenProvider.getMemberByAccessToken(request);

        return memberService.deleteMember(memberDto.getEmail());
    }

    // 사진 파일 삭제용 테스트
//    @PostMapping
//    public void test() throws IOException {
//        String url = "";
//
//        s3Service.deleteFile(url.split("/")[3]);
//    }


}
