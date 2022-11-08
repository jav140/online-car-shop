package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jl.domains.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
