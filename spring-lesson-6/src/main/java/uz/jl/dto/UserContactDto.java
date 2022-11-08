package uz.jl.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactDto {

    private String username;
    private String phoneNumber;

}
