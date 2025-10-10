package vn.globits.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.User;
public interface UserRepository extends JpaRepository<User, Long>  {
    boolean existsByEmail(String email);
}
