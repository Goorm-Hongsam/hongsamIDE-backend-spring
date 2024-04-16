package hongsam.api.member.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

    private String email;
    private String username;
    private String uuid;
    private String profileUrl;
    private String authorities;
}
