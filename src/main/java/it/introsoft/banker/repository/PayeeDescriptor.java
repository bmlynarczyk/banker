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
@Table(name = "payee_descriptor")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PayeeDescriptor {

    @Id
    @GeneratedValue
    private Long id;
    private String fromAccount;
    private String transferType;
    private String name;
    private String account;
    private String address;
    private String category;

}
