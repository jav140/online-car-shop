package uz.jl.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    @NotEmpty(message = "username required")
    private String username;

    @NotEmpty(message = "password required")
    private String password;

}
