package it.introsoft.banker;

import it.introsoft.banker.repository.H2Transfer;
import it.introsoft.banker.repository.TransferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static it.introsoft.banker.repository.QH2Transfer.h2Transfer;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"pko", "storage.h2"})
public class PkoImportTest {

    @Autowired
    TransferRepository transferRepository;

    @Test
    public void doesStart() throws Exception {
        Iterable<H2Transfer> all = transferRepository.findAll(h2Transfer.date.desc(), h2Transfer.dateTransferNumber.desc());

        assertThat(all.spliterator().estimateSize()).isEqualTo(2);

        Iterator<H2Transfer> iterator = all.iterator();

        H2Transfer first = iterator.next();
        assertThat(first.getDate().toString()).isEqualTo("2017-03-03 00:00:00.0");
        assertThat(first.getBeneficiaryAccount()).isEqualTo("80 1050 1953 0000 0000 0000 0000");
        H2Transfer second = iterator.next();
        assertThat(second.getDate().toString()).isEqualTo("2017-03-03 00:00:00.0");
        assertThat(second.getBeneficiaryAccount()).isEqualTo("03 2190 1012 0000 0000 0000 0000");
    }

}