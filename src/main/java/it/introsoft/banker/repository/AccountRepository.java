package it.introsoft.banker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "accounts", collectionResourceRel = "accounts")
public interface AccountRepository extends JpaRepository<Account, String>, QueryDslPredicateExecutor<Account> {

    Optional<Account> findByNumber(String number);

}
