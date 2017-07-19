package it.introsoft.banker.model.jpa;

import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.model.raw.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "category_descriptor")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryDescriptor {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String fromAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DescriptorOrigin origin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferType transferType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank bank;

    @Column(nullable = false)
    private String name;

    private String account;

    private String address;

    private String category;

}
