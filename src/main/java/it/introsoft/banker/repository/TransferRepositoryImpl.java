package it.introsoft.banker.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import it.introsoft.banker.model.transfer.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

public class TransferRepositoryImpl implements TransferRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public TransferRepositoryImpl(JpaContext jpaContext) {
        queryFactory = new JPAQueryFactory(jpaContext.getEntityManagerByManagedType(H2Transfer.class));
    }

    @Override
    public void updateBalanceInLaterThanTodayTransfers(Transfer transfer) {
        queryFactory.update(QH2Transfer.h2Transfer)
                .where(QH2Transfer.h2Transfer.account.eq(transfer.getAccount()).and(QH2Transfer.h2Transfer.date.gt(transfer.getDate())))
                .set(QH2Transfer.h2Transfer.balance, QH2Transfer.h2Transfer.balance.add(transfer.getAmount()));
    }

    @Override
    public void updateBalanceInTodayTransfers(Transfer transfer) {
        queryFactory.update(QH2Transfer.h2Transfer).
                where(QH2Transfer.h2Transfer.account.eq(transfer.getAccount())
                        .and(QH2Transfer.h2Transfer.date.eq(transfer.getDate()))
                        .and(QH2Transfer.h2Transfer.dateTransferNumber.gt(transfer.getDateTransferNumber()))).
                set(QH2Transfer.h2Transfer.balance, QH2Transfer.h2Transfer.balance.add(transfer.getAmount()));
    }

}
