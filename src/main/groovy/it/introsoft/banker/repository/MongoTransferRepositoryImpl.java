package it.introsoft.banker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

public class MongoTransferRepositoryImpl implements MongoTransferRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoTransferRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateBalanceInLaterTransfers(String account, long amount, Date date) {
        mongoTemplate.updateMulti(new Query(Criteria.where("account").is(account).andOperator(Criteria.where("date").gt(date))), new Update().inc("balance", amount), MongoTransfer.class);
    }

}
