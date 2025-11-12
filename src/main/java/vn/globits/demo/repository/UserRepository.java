package vn.globits.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.globits.demo.domain.User;
import vn.globits.demo.dto.UserDTO;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>  {
    boolean existsByEmail(String email);
    boolean existsByPerson_Id(Long personId);
}
