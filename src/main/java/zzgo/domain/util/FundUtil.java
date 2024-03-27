package zzgo.domain.util;

import zzgo.domain.FundDetail;

import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-26 21:35
 */
public class FundUtil {
    public static Money getTotalMoney(List<FundDetail> details) {
        return details.stream().map(FundDetail::getAmount).reduce(Money.zero(), Money::add);
    }

    public static double calcRate(List<FundDetail> fund1, List<FundDetail> fund2) {
        Money money2 = getTotalMoney(fund2);
        Money money1 = getTotalMoney(fund1);
        return money2.subtract(money1).multiply(100).divide(money1);
    }

}
