package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.jl.domains.Uploads;

import java.util.Optional;

public interface FileStorageRepository extends JpaRepository<Uploads, Long> {

    Optional<Uploads> findByGeneratedName(String generatedName);

}
