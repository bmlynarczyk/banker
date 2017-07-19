package it.introsoft.banker.repository;

import it.introsoft.banker.model.jpa.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "transfers", collectionResourceRel = "transfers")
public interface TransferRepository extends JpaRepository<Transfer, Long>, QueryDslPredicateExecutor<Transfer>, TransferRepositoryCustom {

    Optional<Transfer> findFirstByAccountOrderByDateDescDateTransferNumberDesc(String account);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByDateAscDateTransferNumberAsc(String account, Date start, Date stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByDateDescDateTransferNumberDesc(String account, Date start, Date stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByBalanceAsc(String account, Date start, Date stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByBalanceDesc(String account, Date start, Date stop);

}
