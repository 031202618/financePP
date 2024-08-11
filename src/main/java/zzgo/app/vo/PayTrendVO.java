package zzgo.app.vo;

import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-25 21:25
 */
public record PayTrendVO(
        double totalMoneyFullRate,
        double totalMoneyFullYearRate,
        double totalMoneyMonthRate,
        double totalMoneyMonthYearRate,
        double stockMoneyFullRate,
        double stockMoneyFullYearRate,
        double stockMoneyMonthRate,
        double stockMoneyMonthYearRate,
        List<TrendDetail> totalMoney,
        List<TrendDetail> stockMoney
) {
    public record TrendDetail(
            Money money,
            Money stackMoney,
            double rate,
            LocalDate date,
            String beatDesc,
            double beatPercent
    ) {
    }
}
