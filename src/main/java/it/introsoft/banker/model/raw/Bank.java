package it.introsoft.banker.model.raw;

import it.introsoft.banker.service.event.*;

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
    },
    BZ_WBK {
        @Override
        public UpdateTransferCategoryEvent getUpdateTransferCategoryEvent() {
            return new BzWbkUpdateTransferCategoryEvent();
        }
    };

    public abstract UpdateTransferCategoryEvent getUpdateTransferCategoryEvent();

}