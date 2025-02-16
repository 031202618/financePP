package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import zzgo.app.vo.HomePageVO;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.HomePageInfo;
import zzgo.domain.HomePageInfoService;
import zzgo.domain.util.FundUtil;
import zzgo.domain.util.Money;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final HomePageInfoService homePageInfoService;
    private final FundDetailService fundDetailService;

    private static Money getAllFundDiff(List<FundDetail> lastFund, List<FundDetail> firstFund) {
        Money allFundDiff = FundUtil.getTotalMoney(lastFund).subtract(FundUtil.getTotalMoney(firstFund));
        Money lastFundReduction = FundUtil.getTotalMoney(lastFund.stream().filter(FundDetail::isReduction).toList());
        Money firstFundReduction = FundUtil.getTotalMoney(firstFund.stream().filter(FundDetail::isReduction).toList());
        allFundDiff = allFundDiff.subtract(lastFundReduction).add(firstFundReduction);
        return allFundDiff;
    }

    public HomePageVO getHomePageInfo() {
        HomePageInfo homePageInfo = homePageInfoService.get();

        List<FundDetail> allFund = fundDetailService.getAll();
        List<FundDetail> firstFund = FundUtil.getFirstMonthFund(allFund);
        List<FundDetail> lastFund = FundUtil.getLastMonthFund(allFund);
        List<FundDetail> firstStockFund = firstFund.stream().filter(FundDetail::isStock).filter(fundDetail -> !fundDetail.isSalary()).toList();
        List<FundDetail> lastStockExceptReductionFund = lastFund.stream().filter(FundDetail::isStock).filter(x -> !x.isReduction()).toList();
        List<FundDetail> allReduction = allFund.stream().filter(FundDetail::isReduction).toList();
        Money allSalary = FundUtil.getTotalMoney(allFund.stream().filter(FundDetail::isSalary).toList());

        Money allFundDiff = getAllFundDiff(lastFund, firstFund);
        Money stockFundDiff = FundUtil.getTotalMoney(lastStockExceptReductionFund)
                .add(FundUtil.getTotalMoney(allReduction))
                .subtract(FundUtil.getTotalMoney(firstStockFund))
                .subtract(allSalary);

        Map<Integer, Triple<Money, Money, Money>> year2FundSummary = calcYear2FundSummary(allFund);
        return new HomePageVO(
                homePageInfo,
                allFundDiff.toAmount(),
                stockFundDiff.toAmount(),
                allFundDiff.isZero() ? 0 : stockFundDiff.multiply(100).divide(allFundDiff),
                firstFund.getFirst().getAddTime(),
                lastFund.getFirst().getAddTime(),
                year2FundSummary
        );
    }

    /**
     * @param allFund
     * @return {年份: {年初理财资金, 年末理财资金, 整体收益}}
     */
    private Map<Integer, Triple<Money, Money, Money>> calcYear2FundSummary(List<FundDetail> allFund) {
        Map<Integer, List<FundDetail>> year2FundDetail = allFund.stream().collect(Collectors.groupingBy(x -> x.getAddTime().getYear()));
        Map<Integer, Triple<Money, Money, Money>> year2FundSummary = new HashMap<>();
        for (Map.Entry<Integer, List<FundDetail>> yearAndDetail : year2FundDetail.entrySet()) {
            Integer year = yearAndDetail.getKey();
            List<FundDetail> details = yearAndDetail.getValue();
            List<FundDetail> firstFund = FundUtil.getFirstMonthFund(allFund);
            List<FundDetail> lastFund = FundUtil.getLastMonthFund(allFund);
            List<FundDetail> firstStockFund = firstFund.stream().filter(FundDetail::isStock).filter(fundDetail -> !fundDetail.isSalary()).toList();
            List<FundDetail> lastStockExceptReductionFund = lastFund.stream().filter(FundDetail::isStock).filter(x -> !x.isReduction()).toList();
            List<FundDetail> allReduction = allFund.stream().filter(FundDetail::isReduction).toList();
            Money allSalary = FundUtil.getTotalMoney(allFund.stream().filter(FundDetail::isSalary).toList());
            Money stockFundDiff = FundUtil.getTotalMoney(lastStockExceptReductionFund)
                    .add(FundUtil.getTotalMoney(allReduction))
                    .subtract(FundUtil.getTotalMoney(firstStockFund))
                    .subtract(allSalary);
//            year2FundSummary.put(year, );
        }
        return year2FundSummary;
    }
}
