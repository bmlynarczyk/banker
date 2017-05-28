package it.introsoft.banker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "transfers", collectionResourceRel = "transfers")
public interface TransferRepository extends JpaRepository<H2Transfer, Long>, QueryDslPredicateExecutor<H2Transfer>, TransferRepositoryCustom {
}
