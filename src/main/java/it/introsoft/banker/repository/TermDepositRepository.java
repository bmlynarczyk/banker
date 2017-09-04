package it.introsoft.banker.repository;

import it.introsoft.banker.model.jpa.QTermDeposit;
import it.introsoft.banker.model.jpa.TermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(path = "termDeposits", collectionResourceRel = "termDeposits")
public interface TermDepositRepository extends JpaRepository<TermDeposit, Long>, QueryDslPredicateExecutor<QTermDeposit> {

}
