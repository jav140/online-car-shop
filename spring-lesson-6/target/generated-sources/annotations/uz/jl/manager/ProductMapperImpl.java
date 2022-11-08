package uz.jl.manager;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.jl.domains.Product;
import uz.jl.dto.ProductCreateDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-09T01:06:42+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product fromCreateDto(ProductCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( dto.getId() );
        product.category( dto.getCategory() );
        product.name( dto.getName() );
        product.price( dto.getPrice() );

        return product.build();
    }
}
