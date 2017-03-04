package it.introsoft.banker.model.transfer.raw

import groovy.transform.CompileStatic
import it.introsoft.banker.model.transfer.Transfer

@CompileStatic
interface TransferRaw {

    Transfer asTransfer()

}