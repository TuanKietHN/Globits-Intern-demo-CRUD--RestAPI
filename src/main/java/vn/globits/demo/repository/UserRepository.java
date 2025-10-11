package vn.globits.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.globits.demo.domain.User;
public interface UserRepository extends JpaRepository<User, Long>  {
    boolean existsByEmail(String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.person.id = :personId")
    boolean existsByPersonId(@Param("personId") Long personId);
}
