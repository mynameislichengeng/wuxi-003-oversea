package com.wizarpos.pay.recode.constants;

import com.wizarpos.recode.constants.TransRecordLogicConstants;

public interface SaleConstants {


    enum InvoiceAuthority {
        NEED("1"),
        UNNEED("0"),
        ;
        String flag;

        public String getFlag() {
            return flag;
        }

        InvoiceAuthority(String flag) {
            this.flag = flag;
        }

        public static InvoiceAuthority getAuthority(String flag) {
            InvoiceAuthority[] imageFormatTypes = values();
            for (InvoiceAuthority imageFormatType : imageFormatTypes) {
                if (imageFormatType.getFlag().equals(flag)) {
                    return imageFormatType;
                }
            }
            return NEED;
        }

    }
}
