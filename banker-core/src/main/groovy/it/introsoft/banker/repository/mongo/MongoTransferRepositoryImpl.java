package it.introsoft.banker.repository.mongo;

import it.introsoft.banker.model.transfer.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoTransferRepositoryImpl implements MongoTransferRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoTransferRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateBalanceInLaterThanTodayTransfers(Transfer transfer) {
        mongoTemplate.updateMulti(new Query(
                        Criteria.where("account").is(transfer.getAccount())
                                .andOperator(Criteria.where("date").gt(transfer.getDate()))
                ),
                new Update().inc("balance", transfer.getAmount()), MongoTransfer.class);
    }

    @Override
    public void updateBalanceInTodayTransfers(Transfer transfer) {
        mongoTemplate.updateMulti(new Query(
                        Criteria.where("account").is(transfer.getAccount())
                                .andOperator(Criteria.where("date").is(transfer.getDate())
                                        .andOperator(Criteria.where("dateTransferNumber").gt(transfer.getDateTransferNumber())))
                ),
                new Update().inc("balance", transfer.getAmount()), MongoTransfer.class);
    }

}
