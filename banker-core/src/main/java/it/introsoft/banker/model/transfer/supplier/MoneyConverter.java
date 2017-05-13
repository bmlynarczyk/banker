package it.introsoft.banker.model.transfer.supplier;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class MoneyConverter {

    private static final String PLN_TO_REPLACE_SUFFIX = " PLN";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    public static Long toMoneyValue(String value) {
        DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
        nf.setParseBigDecimal(true);
        return (((BigDecimal) nf.parse(value, new ParsePosition(0))).multiply(new BigDecimal("1000"))).longValue();
    }

    public static Long bgzOptimaStringToMoneyValue(String value) {
        return toMoneyValue(value.replaceAll(PLN_TO_REPLACE_SUFFIX, EMPTY).replaceAll(SPACE, EMPTY));
    }

    public static Long mBankStringToMoneyValue(String value) {
        return bgzOptimaStringToMoneyValue(value);
    }

    public static Long milleniumStringToMoneyValue(String value) {
        return bgzOptimaStringToMoneyValue(value);
    }

}
