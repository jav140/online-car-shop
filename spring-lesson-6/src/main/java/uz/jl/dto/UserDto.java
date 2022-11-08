package uz.jl.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "username required")
    private String username;

    @NotEmpty(message = "password required")
    private String password;

    @NotEmpty(message = "Confirm password")
    private String confirmPassword;

}
