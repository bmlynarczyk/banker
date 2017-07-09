package it.introsoft.banker.repository;

import it.introsoft.banker.model.transfer.type.TransferType;
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

    @Column(nullable = false)
    private TransferType transferType;

    @Column(nullable = false)
    private String name;

    private String account;

    private String address;

    private String category;

}
