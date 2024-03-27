package zzgo.app.vo;

import zzgo.domain.util.Money;

import java.time.YearMonth;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-25 21:25
 */
public record PayTrendVO(
        double totalMoneyFullRate,
        double totalMoneyMonthRate,
        double stockMoneyFullRate,
        double stockMoneyMonthRate,
        List<TrendDetail> totalMoney,
        List<TrendDetail> stockMoney
) {
    public record TrendDetail(
            Money money,
            Money stackMoney,
            double rate,
            YearMonth ym
    ) {
    }
}
