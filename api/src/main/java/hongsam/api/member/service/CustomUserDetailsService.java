package hongsam.api.member.service;

import hongsam.api.member.domain.CustomUserDetails;
import hongsam.api.member.domain.Member;
import hongsam.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findPasswordByEmail(username);

        // 이메일에대한 회원 정보가 없을 때 (존재하지 않는 회원)
        if (member != null) {
            return new CustomUserDetails(member);
        } else {
            throw new UsernameNotFoundException("User not found with userEmail: " + username);
        }
    }
}
