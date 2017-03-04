package it.introsoft.banker.repository.h2

import com.querydsl.jpa.impl.JPAQueryFactory
import it.introsoft.banker.model.transfer.Transfer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaContext

import static it.introsoft.banker.repository.h2.QH2Transfer.h2Transfer

public class H2TransferRepositoryImpl implements H2TransferRepositoryCustom {

    private final JPAQueryFactory queryFactory

    @Autowired
    H2TransferRepositoryImpl(JpaContext jpaContext) {
        queryFactory = new JPAQueryFactory(jpaContext.getEntityManagerByManagedType(H2Transfer))
    }

    @Override
    public void updateBalanceInLaterThanTodayTransfers(Transfer transfer) {
        queryFactory.update(h2Transfer)
                .where(h2Transfer.account.eq(transfer.account) & h2Transfer.date.gt(transfer.date))
                .set(h2Transfer.balance, h2Transfer.balance.add(transfer.amount))
    }

    @Override
    public void updateBalanceInTodayTransfers(Transfer transfer) {
        queryFactory.update(h2Transfer).
                where(h2Transfer.account.eq(transfer.account)
                        & h2Transfer.date.eq(transfer.date)
                        & h2Transfer.dateTransferNumber.gt(transfer.dateTransferNumber)).
                set(h2Transfer.balance, h2Transfer.balance.add(transfer.amount))
    }

}
