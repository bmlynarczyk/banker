package it.introsoft.banker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(path = "transfers", collectionResourceRel = "transfers")
public interface TransferRepository extends JpaRepository<H2Transfer, Long>, QueryDslPredicateExecutor<H2Transfer>, TransferRepositoryCustom {

    Optional<H2Transfer> findFirstByAccountOrderByDateDescDateTransferNumberDesc(String account);

    Optional<H2Transfer> findFirstByAccountAndDateBetweenOrderByDateAscDateTransferNumberAsc(String account, Date start, Date stop);

    Optional<H2Transfer> findFirstByAccountAndDateBetweenOrderByDateDescDateTransferNumberDesc(String account, Date start, Date stop);

    Optional<H2Transfer> findFirstByAccountAndDateBetweenOrderByBalanceAsc(String account, Date start, Date stop);

    Optional<H2Transfer> findFirstByAccountAndDateBetweenOrderByBalanceDesc(String account, Date start, Date stop);

}
