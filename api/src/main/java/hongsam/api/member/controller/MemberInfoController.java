package hongsam.api.member.controller;

import hongsam.api.member.domain.MemberResponse;
import hongsam.api.member.domain.MemberUpdateDto;
import hongsam.api.member.domain.PasswordCheckDto;
import hongsam.api.member.repository.MemberRepository;
import hongsam.api.member.service.MemberService;
import hongsam.api.member.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/mypage")
public class MemberInfoController {

    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 프로필 사진 수정
    @PostMapping("/profile-img")
    public MemberResponse updateProfileImg(@RequestParam("profileImg") MultipartFile multipartFile) throws IOException {

//        Member member = memberRepository.findMemberByEmailOne(loginMember.getEmail());

//      s3에 파일 올리기
//        String imgUrl = s3Service.uploadFiles(member.getUuid(), multipartFile, "profileImage");

//        loginMember.setProfileUrl(imgUrl);

//        return new MemberResponse(200,imgUrl);
        return null;
    }

    // 회원 정보 수정
    @PutMapping("/info")
    public MemberResponse updateMemberInfo(@RequestBody MemberUpdateDto memberUpdateDto) {

//        return memberService.updateMemberInfo(loginMember, memberUpdateDto.getUsername(),memberUpdateDto.getPassword());
        return null;
    }

    // 비밀번호 확인
    @PostMapping("/pw-check")
    public MemberResponse checkPassword(@RequestBody PasswordCheckDto passwordCheckDto) {

//        return memberService.checkPassword(passwordCheckDto.getPassword(), loginMember.getEmail());
        return null;
    }

    // 회원 탙퇴
    @DeleteMapping("/members")
    public MemberResponse deleteMember() {

//        return memberService.deleteMember(loginMember.getEmail());
        return null;
    }

    // 사진 파일 삭제용 테스트
//    @PostMapping
//    public void test() throws IOException {
//        String url = "";
//
//        s3Service.deleteFile(url.split("/")[3]);
//    }


}
