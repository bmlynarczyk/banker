package it.introsoft.banker.repository;

import it.introsoft.banker.model.jpa.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@CrossOrigin
@RepositoryRestResource(path = "transfers", collectionResourceRel = "transfers")
public interface TransferRepository extends JpaRepository<Transfer, Long>, QueryDslPredicateExecutor<Transfer>, TransferRepositoryCustom {

    Optional<Transfer> findFirstByAccountOrderByDateDescDateTransferNumberDesc(String account);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByDateAscDateTransferNumberAsc(String account, LocalDate start, LocalDate stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByDateDescDateTransferNumberDesc(String account, LocalDate start, LocalDate stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByBalanceAsc(String account, LocalDate start, LocalDate stop);

    Optional<Transfer> findFirstByAccountAndDateBetweenOrderByBalanceDesc(String account, LocalDate start, LocalDate stop);

    List<Transfer> findByCategoryAndDateBetweenOrderByDateDescDateTransferNumberDesc(@Param("category") String category,
                                                                                     @DateTimeFormat(iso = DATE) @Param("start") LocalDate start,
                                                                                     @DateTimeFormat(iso = DATE) @Param("stop") LocalDate stop);

}
