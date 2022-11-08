package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.jl.domains.Basket;
import uz.jl.domains.Product;

import javax.persistence.EntityManager;
import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
//    @Query(
//            value = "SELECT * FROM role WHERE id NOT IN (SELECT role_id FROM user_role WHERE user_id = ?1)",
//            nativeQuery = true
//    )

//    Query query = em.createNativeQuery("{call getEmployeeDetails(?,?)}",
//                    EmployeeDetails.class)
//            .setParameter(1, employeeId)
//            .setParameter(2, companyId);
//
//    List<EmployeeDetails> result = query.getResultList();


//    @Modifying
//    @Query(value = "update Basket b set b.status = 'confirmed' where b.username=:username")
//    int updateToConfirm( String username);

    @Transactional
    @Modifying
    @Query(value = "update Basket set status='confirmed' where username=:u_name")
    void updateStatus(@Param("u_name") String username);

//    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category, ' ', p.price, ' ', p.createdBy) LIKE %?1%")
//    List<Product> search(String keyword);

//
//    @Modifying
//    @Query(value = "update Users u set u.status = ? where u.name = ?",
//            nativeQuery = true)
//    int updateUserSetStatusForNameNative(Integer status, String name);

//    @Query("SELECT u FROM Basket u WHERE u.username  =: my_username")
//    List<Basket> findAllByUsername(@Param("my_username") String my_username);
//
////    @Query(select p from Person p where p.forename = :forename and p.surname = :surname)
////    User findByForenameAndSurname(@Param("surname") String lastname,
////                                  @Param("forename") String firstname);
////}
}
