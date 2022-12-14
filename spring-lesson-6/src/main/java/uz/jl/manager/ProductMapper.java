package uz.jl.manager;

import org.mapstruct.Mapper;
import uz.jl.domains.Product;
import uz.jl.dto.ProductCreateDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromCreateDto(ProductCreateDto dto);

}
