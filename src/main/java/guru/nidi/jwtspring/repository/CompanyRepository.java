package guru.nidi.jwtspring.repository;

import guru.nidi.jwtspring.domain.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CompanyRepository extends MongoRepository<Company, String> {
}