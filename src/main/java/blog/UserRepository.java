package blog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by cohall on 3/30/2017.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);

    Optional<User> findByUidAndRole(String uid, String role);

}
