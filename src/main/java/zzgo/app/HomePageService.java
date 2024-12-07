package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.app.vo.HomePageVO;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.HomePageInfo;
import zzgo.domain.HomePageInfoService;
import zzgo.domain.util.FundUtil;
import zzgo.domain.util.Money;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final HomePageInfoService homePageInfoService;
    private final FundDetailService fundDetailService;

    public HomePageVO getHomePageInfo() {
        HomePageInfo homePageInfo = homePageInfoService.get();

        List<FundDetail> allFund = fundDetailService.getAll();
        List<FundDetail> firstFund = FundUtil.getFirstMonthFund(allFund);
        List<FundDetail> lastFund = FundUtil.getLastMonthFund(allFund);
        List<FundDetail> firstStockFund = firstFund.stream().filter(FundDetail::isStock).filter(fundDetail -> !fundDetail.isSalary()).toList();
        List<FundDetail> lastStockFund = lastFund.stream().filter(FundDetail::isStock).toList();
        Money allSalary = FundUtil.getTotalMoney(allFund.stream().filter(FundDetail::isSalary).toList());

        Money allFundDiff = getAllFundDiff(lastFund, firstFund);
        Money stockFundDiff = FundUtil.getTotalMoney(lastStockFund).subtract(FundUtil.getTotalMoney(firstStockFund)).subtract(allSalary);
        return new HomePageVO(
                homePageInfo,
                allFundDiff.toAmount(),
                stockFundDiff.toAmount(),
                allFundDiff.isZero() ? 0 : stockFundDiff.multiply(100).divide(allFundDiff),
                firstFund.getFirst().getAddTime(),
                lastFund.getFirst().getAddTime()
        );
    }

    private static Money getAllFundDiff(List<FundDetail> lastFund, List<FundDetail> firstFund) {
        Money allFundDiff = FundUtil.getTotalMoney(lastFund).subtract(FundUtil.getTotalMoney(firstFund));
        Money lastFundReduction = FundUtil.getTotalMoney(lastFund.stream().filter(FundDetail::isReduction).toList());
        Money firstFundReduction = FundUtil.getTotalMoney(firstFund.stream().filter(FundDetail::isReduction).toList());
        allFundDiff = allFundDiff.subtract(lastFundReduction).add(firstFundReduction);
        return allFundDiff;
    }
}
