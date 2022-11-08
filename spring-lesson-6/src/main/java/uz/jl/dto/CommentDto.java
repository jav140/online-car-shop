package uz.jl.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    @NotEmpty(message = "comment body required")
    private String body;
    private Long id;


}
