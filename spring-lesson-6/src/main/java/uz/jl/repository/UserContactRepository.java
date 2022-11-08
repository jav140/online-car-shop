package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.jl.domains.UserContact;

public interface UserContactRepository extends JpaRepository<UserContact,Long> {

}
