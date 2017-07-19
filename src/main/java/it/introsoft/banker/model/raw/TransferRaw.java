package it.introsoft.banker.model.raw;

import it.introsoft.banker.model.jpa.Transfer;

public interface TransferRaw {
    Transfer asTransfer();
}