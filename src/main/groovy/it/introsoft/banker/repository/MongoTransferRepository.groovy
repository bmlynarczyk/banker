package it.introsoft.banker.repository

import groovy.transform.CompileStatic
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

@CompileStatic
interface MongoTransferRepository extends MongoRepository<MongoTransfer, String>, QueryDslPredicateExecutor<MongoTransfer> {
}