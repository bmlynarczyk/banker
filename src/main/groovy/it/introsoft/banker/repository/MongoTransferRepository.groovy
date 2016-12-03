package it.introsoft.banker.repository

import it.introsoft.banker.model.transfer.Transfer
import org.springframework.data.repository.PagingAndSortingRepository

interface MongoTransferRepository extends PagingAndSortingRepository<Transfer, String>{
}