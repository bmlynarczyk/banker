package it.introsoft.banker.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "transfer")
@Getter
@Setter
@ToString
public class MongoTransfer {

    @Id
    private String id;
    private Date date;
    private Long dateTransferNumber;
    private String description;
    private Long amount;
    private Long balance;
    private String currency;
    private String transferType;
    private String account;
    private String beneficiaryAccount;
    private String bank;
    private String category;
    private List<String> tags;

}
