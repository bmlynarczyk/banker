package it.introsoft.banker.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "beneficiary_descriptor")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BeneficiaryDescriptor {

    @Id
    @GeneratedValue
    private Long id;
    private String forAccount;
    private String transferType;
    private String name;
    private String account;
    private String address;
    private String bank;

}
