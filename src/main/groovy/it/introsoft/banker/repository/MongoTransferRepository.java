package it.introsoft.banker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface MongoTransferRepository extends MongoRepository<MongoTransfer, String>, QueryDslPredicateExecutor<MongoTransfer>, MongoTransferRepositoryCustom {
}
