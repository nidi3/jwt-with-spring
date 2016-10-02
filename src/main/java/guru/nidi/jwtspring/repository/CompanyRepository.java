package guru.nidi.jwtspring.repository;

import guru.nidi.jwtspring.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource
public interface CompanyRepository extends MongoRepository<Company, String> {
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Company> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Company> findAll(Sort sort);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<Company> findAll(Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.companyId == #id")
    Company findOne(@Param("id") String id);
}