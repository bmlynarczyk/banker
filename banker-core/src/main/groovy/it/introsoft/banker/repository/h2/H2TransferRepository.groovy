package it.introsoft.banker.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface H2TransferRepository extends JpaRepository<H2Transfer, Long>, QueryDslPredicateExecutor<H2Transfer>, H2TransferRepositoryCustom {
}
