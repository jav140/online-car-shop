package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.jl.domains.ProductContact;

import java.util.List;

@Repository
public interface ProductContactRepository extends JpaRepository<ProductContact,Long> {

    @Transactional
    @Query(value = "select json_agg(json_build_object('phoneNumber',UserContact.phoneNumber , 'username',UserContact .username,'product_id',Basket.product_id))::text from Basket inner join UserContact on Basket.username=UserContact.username",nativeQuery = true)
    String findAllProductContact();
}
