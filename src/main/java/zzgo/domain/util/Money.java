package zzgo.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhengw
 * @since 2024-03-05 15:00
 */
public record Money(
        long amount
) {

    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).intValue());
    }

    public static Money zero() {
        return new Money(0);
    }

    public String toString() {
        return amount / 100.0 + "";
    }

    public String toAmount() {
        return toString();
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }

    public Money subtract(Money money) {
        return new Money(this.amount - money.amount);
    }

    public Money multiply(int num) {
        return new Money(this.amount * num);
    }

    public Money divide(int num) {
        return new Money(BigDecimal.valueOf(this.amount).divide(BigDecimal.valueOf(num), 0, DEFAULT_ROUNDING_MODE).intValue());
    }

    public int divide(Money money) {
        return BigDecimal.valueOf(this.amount).divide(BigDecimal.valueOf(money.amount), 0, DEFAULT_ROUNDING_MODE).intValue();
    }

}
