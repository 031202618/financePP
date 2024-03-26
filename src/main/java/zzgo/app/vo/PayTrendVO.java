package zzgo.app.vo;

import zzgo.domain.util.Money;

import java.time.YearMonth;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-25 21:25
 */
public record PayTrendVO(
        int totalMoneyFullRate,
        int totalMoneyMonthRate,
        int stockMoneyFullRate,
        int stockMoneyMonthRate,
        List<TrendDetail> totalMoney,
        List<TrendDetail> stockMoney
) {
    public record TrendDetail(
            Money money,
            int rate,
            YearMonth ym
    ) {
    }
}
