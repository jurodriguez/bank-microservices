package com.test.account.application.utils;

import java.math.BigDecimal;

public class FormatUtils {

    public static String formatAmount(BigDecimal value) {
        if (value == null) return null;

        BigDecimal normalized = value.stripTrailingZeros();
        return normalized.toPlainString();
    }
}
