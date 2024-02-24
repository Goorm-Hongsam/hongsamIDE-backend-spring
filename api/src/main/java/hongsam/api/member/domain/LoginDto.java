package hongsam.api.member.domain;

import lombok.Data;

@Data
public class LoginDto {

    private String email;
    private String password;
}
