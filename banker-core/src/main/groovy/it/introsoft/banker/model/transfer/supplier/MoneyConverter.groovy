package it.introsoft.banker.model.transfer.supplier

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParsePosition

class MoneyConverter {

    private static final String PLN_TO_REPLACE_SUFFIX = ' PLN'
    private static final String SPACE = ' '
    private static final String EMPTY = ''

    static Long toMoneyValue(String value) {
        DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"))
        nf.setParseBigDecimal(true)
        return (((BigDecimal) nf.parse(value, new ParsePosition(0))) * (new BigDecimal("1000"))).longValue()
    }

    static Long bgzOptimaStringToMoneyValue(String value) {
        return toMoneyValue(value.replaceAll(PLN_TO_REPLACE_SUFFIX, EMPTY).replaceAll(SPACE, EMPTY))
    }

    static Long mBankStringToMoneyValue(String value) {
        return bgzOptimaStringToMoneyValue(value)
    }

    static Long milleniumStringToMoneyValue(String value) {
        return bgzOptimaStringToMoneyValue(value)
    }

}
