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
        List<FundDetail> firstStockFund = firstFund.stream().filter(FundDetail::isStock).toList();
        List<FundDetail> lastStockFund = lastFund.stream().filter(FundDetail::isStock).toList();

        Money allFundDiff = FundUtil.getTotalMoney(lastFund).subtract(FundUtil.getTotalMoney(firstFund));
        Money stockFundDiff = FundUtil.getTotalMoney(lastStockFund).subtract(FundUtil.getTotalMoney(firstStockFund));
        return new HomePageVO(
                homePageInfo,
                allFundDiff.toAmount(),
                stockFundDiff.toAmount(),
                allFundDiff.isZero()? 0 : stockFundDiff.multiply(100).divide(allFundDiff),
                firstFund.getFirst().getAddTime(),
                lastFund.getFirst().getAddTime()
        );
    }
}
