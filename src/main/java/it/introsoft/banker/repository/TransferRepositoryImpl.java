package it.introsoft.banker.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import it.introsoft.banker.model.jpa.QTransfer;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.model.raw.TransferType;
import it.introsoft.banker.view.AccountReportTransfer;
import it.introsoft.banker.view.CategorySum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TransferRepositoryImpl implements TransferRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QTransfer qtransfer = QTransfer.transfer;

    private final Function<Tuple, AccountReportTransfer> toAccountReportTransfer = tuple -> AccountReportTransfer.builder()
            .transferType(tuple.get(qtransfer.transferType))
            .amount(tuple.get(qtransfer.amount))
            .balance(tuple.get(qtransfer.balance))
            .date(tuple.get(qtransfer.date))
            .dateTransferNumber(tuple.get(qtransfer.dateTransferNumber))
            .build();

    private Function<Tuple, TransferType> transferTypeTransformer = o -> o.get(qtransfer.transferType);
    private Function<Tuple, Long> amountTransformer = o -> o.get(qtransfer.amount.sum());
    private Function<Tuple, Long> idTransformer = o -> o.get(qtransfer.id.count());

    @Autowired
    public TransferRepositoryImpl(JpaContext jpaContext) {
        queryFactory = new JPAQueryFactory(jpaContext.getEntityManagerByManagedType(Transfer.class));
    }

    @Override
    @Transactional
    public long setCategoryByDescriptionStartingWith(String category, String descriptor, TransferType transferType, Bank bank) {
        return queryFactory.update(qtransfer)
                .where(qtransfer.category.isNull()
                        .and(qtransfer.bank.eq(bank))
                        .and(qtransfer.transferType.eq(transferType))
                        .and(qtransfer.description.startsWith(descriptor)))
                .set(qtransfer.category, category).execute();
    }

    @Override
    @Transactional
    public long setCategoryByDescriptionEndingWith(String category, String descriptor, TransferType transferType, Bank bank) {
        return queryFactory.update(qtransfer)
                .where(qtransfer.category.isNull()
                        .and(qtransfer.bank.eq(bank))
                        .and(qtransfer.transferType.eq(transferType))
                        .and(qtransfer.description.endsWith(descriptor)))
                .set(qtransfer.category, category).execute();
    }

    @Override
    @Transactional
    public long setCategoryByBeneficiary(String category, String beneficiary, TransferType transferType, Bank bank) {
        return queryFactory.update(qtransfer)
                .where(qtransfer.category.isNull()
                        .and(qtransfer.beneficiaryName.eq(beneficiary))
                        .and(qtransfer.bank.eq(bank))
                        .and(qtransfer.transferType.eq(transferType)))
                .set(qtransfer.category, category).execute();
    }

    @Override
    @Transactional
    public long setCategoryByPayee(String category, String payee, TransferType transferType, Bank bank) {
        return queryFactory.update(qtransfer)
                .where(qtransfer.category.isNull()
                        .and(qtransfer.beneficiaryName.eq(payee))
                        .and(qtransfer.bank.eq(bank))
                        .and(qtransfer.transferType.eq(transferType)))
                .set(qtransfer.category, category).execute();
    }

    @Override
    public List<AccountReportTransfer> getByPeriod(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        qtransfer.transferType,
                        qtransfer.amount,
                        qtransfer.balance,
                        qtransfer.date,
                        qtransfer.dateTransferNumber
                )
                .from(qtransfer)
                .where(qtransfer.account.eq(account).and(qtransfer.date.between(start, stop)))
                .orderBy(qtransfer.date.asc(), qtransfer.dateTransferNumber.asc())
                .fetch()
                .stream()
                .map(toAccountReportTransfer)
                .collect(toList());
    }

    @Override
    public List<CategorySum> getSumByCategories(Date start, Date stop) {
        return queryFactory
                .select(
                        qtransfer.date.year(),
                        qtransfer.date.month(),
                        qtransfer.category,
                        qtransfer.id.count(),
                        qtransfer.amount.sum()
                )
                .from(qtransfer)
                .where(
                        qtransfer.date.between(start, stop)
                                .and(qtransfer.category.isNotNull())
                )
                .groupBy(
                        qtransfer.date.year(),
                        qtransfer.date.month(),
                        qtransfer.category
                )
                .orderBy(qtransfer.date.year().asc(), qtransfer.date.month().asc())
                .fetch()
                .stream()
                .map(tuple -> CategorySum.builder()
                        .year(tuple.get(qtransfer.date.year()))
                        .month(tuple.get(qtransfer.date.month()))
                        .category(tuple.get(qtransfer.category))
                        .transferCount(tuple.get(qtransfer.id.count()))
                        .transferAmountSum(tuple.get(qtransfer.amount.sum()))
                        .build())
                .collect(toList());
    }

    @Override
    public List<CategorySum> getSumByCategories(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        qtransfer.date.year(),
                        qtransfer.date.month(),
                        qtransfer.category,
                        qtransfer.id.count(),
                        qtransfer.amount.sum()
                )
                .from(qtransfer)
                .where(
                        qtransfer.account.eq(account)
                                .and(qtransfer.date.between(start, stop))
                                .and(qtransfer.category.isNotNull())
                )
                .groupBy(
                        qtransfer.date.year(),
                        qtransfer.date.month(),
                        qtransfer.category
                )
                .orderBy(qtransfer.date.year().asc(), qtransfer.date.month().asc())
                .fetch()
                .stream()
                .map(tuple -> CategorySum.builder()
                        .year(tuple.get(qtransfer.date.year()))
                        .month(tuple.get(qtransfer.date.month()))
                        .category(tuple.get(qtransfer.category))
                        .transferCount(tuple.get(qtransfer.id.count()))
                        .transferAmountSum(tuple.get(qtransfer.amount.sum()))
                        .build())
                .collect(toList());
    }

    @Override
    public Map<TransferType, Long> getAmountSumByTransferType(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        qtransfer.transferType,
                        qtransfer.amount.sum()
                )
                .from(qtransfer)
                .where(qtransfer.account.eq(account).and(qtransfer.date.between(start, stop)))
                .groupBy(qtransfer.transferType)
                .fetch()
                .stream()
                .collect(toMap(transferTypeTransformer, amountTransformer));
    }

    @Override
    public Map<TransferType, Long> getTransferCountByTransferType(String account, Date start, Date stop) {
        return queryFactory
                .select(
                        qtransfer.transferType,
                        qtransfer.id.count()
                )
                .from(qtransfer)
                .where(qtransfer.account.eq(account).and(qtransfer.date.between(start, stop)))
                .groupBy(qtransfer.transferType)
                .fetch()
                .stream()
                .collect(toMap(transferTypeTransformer, idTransformer));
    }

}
