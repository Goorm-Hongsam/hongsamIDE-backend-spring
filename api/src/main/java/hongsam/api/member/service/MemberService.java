package hongsam.api.member.service;

import hongsam.api.member.domain.Member;
import hongsam.api.member.domain.MemberDto;
import hongsam.api.member.domain.MemberResponse;
import hongsam.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public ResponseEntity<String> signup(Member member) {

        List<Member> dbMember = memberRepository.findMemberByEmail(member.getEmail());

        if (!dbMember.isEmpty()) {
            return new ResponseEntity<>("이미 사용중인 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }
        // uuid 생성
        member.setUuid(UUID.randomUUID().toString());
        member.setProfileUrl("https://hongsam-ide.s3.ap-northeast-2.amazonaws.com/profileImage/good.png");
        String password = member.getPassword();
        member.setRole("ROLE_USER");
        // 비밀번호 암호화
        member.setPassword(bCryptPasswordEncoder.encode(password));

        memberRepository.save(member);

        if (member.getId() == null) {
            return new ResponseEntity<>("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    // 회원가입 이메일 확인
    public ResponseEntity<String> emailCheck(String email) {

        List<Member> member = memberRepository.findMemberByEmail(email);

        if (member.isEmpty()) {
            return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("이미 사용중인 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public MemberResponse getUUID(String email) {

        String uuid = memberRepository.findPasswordByEmail(email).getUuid();
        log.info("uuid = {}", uuid);

        return new MemberResponse(200, uuid);

    }

    @Transactional
    public ResponseEntity<Object> updateMemberInfo(MemberDto memberDto, String username, String password) {

        Member member = memberRepository.findMemberByEmailOne(memberDto.getEmail());

        if (username != null && username.trim().isEmpty()) {
            // null X, 빈 문자열 왔을 때
            return new ResponseEntity<>("username이 빈 문자열 입니다.", HttpStatusCode.valueOf(401));
//            return new MemberResponse(401, "username이 빈 문자열 입니다.");
        } else if (username != null) {
            if (member.getUsername().equals(username)) {
                return new ResponseEntity<>("기존의 이름과 동일합니다.", HttpStatusCode.valueOf(403));
//                return new MemberResponse(403, "기존의 이름과 동일합니다.");
            }
            // db update
            member.setUsername(username);
            memberDto.setUsername(username);
        }

        if (password != null && password.trim().isEmpty()) {
            return new ResponseEntity<>("password가 빈 문자열 입니다.", HttpStatusCode.valueOf(401));
//            return new MemberResponse(401, "password가 빈 문자열 입니다.");
        } else if (password != null) {
            if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
                return new ResponseEntity<>("기존의 비밀번호와 동일합니다.", HttpStatusCode.valueOf(402));
//                return new MemberResponse(402, "기존의 비밀번호와 동일합니다.");
            }
            member.setPassword(bCryptPasswordEncoder.encode(password));
        }

        return new ResponseEntity<>(memberDto, HttpStatus.OK);
//        return new MemberResponse(200, memberDto);
    }

    public ResponseEntity<String> checkPassword(String password, String email) {

        Member member = memberRepository.findMemberByEmailOne(email);

        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            return new ResponseEntity<>("비밀번호 일치", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteMember(String email) {

        int deleteResult = memberRepository.deleteMember(email);

        if (deleteResult == 1) {
            return new ResponseEntity<>("회원 탈퇴 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("회원 탈퇴 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}