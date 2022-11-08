package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import uz.jl.domains.AuthUser;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser, Long> , PagingAndSortingRepository<AuthUser,Long> {
    Optional<AuthUser> findByUsername(String username);
}
