package zzgo.domain.util;

import zzgo.domain.FundDetail;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-26 21:35
 */
public class FundUtil {
    public static Money getTotalMoney(List<FundDetail> details) {
        return details.stream().map(FundDetail::getAmount).reduce(Money.zero(), Money::add);
    }

    public static double calcRate(List<FundDetail> parent, List<FundDetail> children) {
        Money money2 = getTotalMoney(children);
        Money money1 = getTotalMoney(parent);
        return money2.subtract(money1).multiply(100).divide(money1);
    }

    public static List<FundDetail> getFirstMonthFund(List<FundDetail> allFund) {
        LocalDate min = allFund.stream().map(FundDetail::getAddTime).min(LocalDate::compareTo).get();
        return allFund.stream().filter(x -> x.getAddTime().equals(min)).toList();
    }

    public static List<FundDetail> getLastMonthFund(List<FundDetail> allFund) {
        LocalDate max = allFund.stream().map(FundDetail::getAddTime).max(LocalDate::compareTo).get();
        return allFund.stream().filter(x -> x.getAddTime().equals(max)).toList();
    }
}
