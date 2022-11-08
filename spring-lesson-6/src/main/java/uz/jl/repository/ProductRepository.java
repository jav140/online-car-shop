package uz.jl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import uz.jl.domains.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category, ' ', p.price, ' ', p.createdBy) LIKE %?1%")
    List<Product> search(String keyword);

//    @Query(value = "from Book t where (lower(t.name) like %:query% or lower(t.genre) like %:query% or lower(t.author) like %:query%) and t.isConfirmed = 'CONFIRMED'")
//    Page<Book> findAll(@Param("query") String searchParam, Pageable pageable)

    @Transactional
    @Query(value = "from Product t where (lower(t.category) like %:query% or lower(t.name) like %:query% or lower(t.price) like %:query%)")
    Page<Product> getAll(@Param("query") String searchParam, Pageable pageable);
}
