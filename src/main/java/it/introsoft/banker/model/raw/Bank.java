package it.introsoft.banker.model.raw;

import it.introsoft.banker.service.event.MilleniumUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.PkoBpUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.UnknownUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.UpdateTransferCategoryEvent;

public enum Bank {

    BGZ_OPTIMA {
        @Override
        public UpdateTransferCategoryEvent getUpdateTransferCategoryEvent() {
            return new UnknownUpdateTransferCategoryEvent();
        }
    },
    PKO_BP {
        @Override
        public UpdateTransferCategoryEvent getUpdateTransferCategoryEvent() {
            return new PkoBpUpdateTransferCategoryEvent();
        }
    },
    M_BANK {
        @Override
        public UpdateTransferCategoryEvent getUpdateTransferCategoryEvent() {
            return new UnknownUpdateTransferCategoryEvent();
        }
    },
    MILLENIUM {
        @Override
        public UpdateTransferCategoryEvent getUpdateTransferCategoryEvent() {
            return new MilleniumUpdateTransferCategoryEvent();
        }
    };

    public abstract UpdateTransferCategoryEvent getUpdateTransferCategoryEvent();

}