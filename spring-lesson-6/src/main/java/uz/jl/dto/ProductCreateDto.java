package uz.jl.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {
    private Long id;
    private String category;
    private String name;
    private Long price;
}
