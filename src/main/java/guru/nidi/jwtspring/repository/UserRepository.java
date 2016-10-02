package guru.nidi.jwtspring.repository;

import guru.nidi.jwtspring.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(@Param("username") String username);
}