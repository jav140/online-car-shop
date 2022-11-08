package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.jl.domains.AuthRole;

public interface AuthPermissionRepository extends JpaRepository<AuthRole, Long> {
}
