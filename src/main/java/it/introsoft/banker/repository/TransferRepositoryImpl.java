package it.introsoft.banker.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.view.AccountReportTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static it.introsoft.banker.repository.QH2Transfer.h2Transfer;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TransferRepositoryImpl implements TransferRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final Function<Tuple, AccountReportTransfer> toAccountReportTransfer = tuple -> AccountReportTransfer.builder()
            .transferType(tuple.get(h2Transfer.transferType))
            .amount(tuple.get(h2Transfer.amount))
            .balance(tuple.get(h2Transfer.balance))
            .date(tuple.get(h2Transfer.date))
            .dateTransferNumber(tuple.get(h2Transfer.dateTransferNumber))
            .build();

    private Function<Tuple, String> transferTypeTransformer = o -> o.get(h2Transfer.transferType);
    private Function<Tuple, Long> amountTransformer = o -> o.get(h2Transfer.amount.sum());

    @Autowired
    public TransferRepositoryImpl(JpaContext jpaContext) {
        queryFactory = new JPAQueryFactory(jpaContext.getEntityManagerByManagedType(H2Transfer.class));
    }

    @Override
    public void updateBalanceInLaterThanTodayTransfers(Transfer transfer) {
        queryFactory.update(h2Transfer)
                .where(h2Transfer.account.eq(transfer.getAccount()).and(h2Transfer.date.gt(transfer.getDate())))
                .set(h2Transfer.balance, h2Transfer.balance.add(transfer.getAmount()));
    }

    @Override
    public void updateBalanceInTodayTransfers(Transfer transfer) {
        queryFactory.update(h2Transfer).
                where(h2Transfer.account.eq(transfer.getAccount())
                        .and(h2Transfer.date.eq(transfer.getDate()))
                        .and(h2Transfer.dateTransferNumber.gt(transfer.getDateTransferNumber()))).
                set(h2Transfer.balance, h2Transfer.balance.add(transfer.getAmount()));
    }

    @Override
    public List<AccountReportTransfer> getByPeriod(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        h2Transfer.transferType,
                        h2Transfer.amount,
                        h2Transfer.balance,
                        h2Transfer.date,
                        h2Transfer.dateTransferNumber
                )
                .from(h2Transfer)
                .where(h2Transfer.account.eq(account).and(h2Transfer.date.between(start, stop)))
                .orderBy(h2Transfer.date.asc(), h2Transfer.dateTransferNumber.asc())
                .fetch()
                .stream()
                .map(toAccountReportTransfer)
                .collect(toList());
    }

    @Override
    public Map<String, Long> getAmountSumByTransferType(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        h2Transfer.transferType,
                        h2Transfer.amount.sum()
                )
                .from(h2Transfer)
                .where(h2Transfer.account.eq(account).and(h2Transfer.date.between(start, stop)))
                .groupBy(h2Transfer.transferType)
                .fetch()
                .stream()
                .collect(toMap(transferTypeTransformer, amountTransformer));
    }

}
