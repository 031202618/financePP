package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import zzgo.app.vo.PayTrendVO;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.util.FundUtil;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayTrendService {

    private final FundDetailService fundDetailService;

    public PayTrendVO getPayAnalysisVo(YearMonth startYM, YearMonth endYM) {
        LocalDate startTime = LocalDate.of(startYM.getYear(), startYM.getMonth(), 1);
        LocalDate endTime = LocalDate.of(endYM.getYear(), endYM.getMonth(), 1).plusMonths(1).minusDays(1);
        var ym2Date2Fund = fundDetailService.getAll().stream()
                .filter(x -> !x.getAddTime().isBefore(startTime) && !x.getAddTime().isAfter(endTime))
                .collect(Collectors.groupingBy(fundDetail -> YearMonth.of(fundDetail.getAddTime().getYear(), fundDetail.getAddTime().getMonth()), Collectors.groupingBy(FundDetail::getAddTime)));
        check(ym2Date2Fund);
        List<List<FundDetail>> totalFund = fillTotalFund(ym2Date2Fund);
        List<Pair<List<FundDetail>, List<FundDetail>>> stockFund = fillStockFund(ym2Date2Fund);
        return buildPayTrendVO(totalFund, stockFund);
    }

    private PayTrendVO buildPayTrendVO(List<List<FundDetail>> totalFund, List<Pair<List<FundDetail>, List<FundDetail>>> stockFund) {
        int totalMoneyFullRate;
        int totalMoneyMonthRate;
        int stockMoneyFullRate;
        int stockMoneyMonthRate;
        List<PayTrendVO.TrendDetail> totalMoney = new ArrayList<>();
        List<PayTrendVO.TrendDetail> stockMoney = new ArrayList<>();
        if (totalFund.size() < 2) {
            totalMoneyFullRate = 0;
        } else {
            totalMoneyFullRate = FundUtil.calcRate(totalFund.get(0), totalFund.get(totalFund.size() - 1));
        }
        if (stockFund.isEmpty()) {
            stockMoneyFullRate = 0;
        } else if (stockFund.size() == 1) {
            stockMoneyFullRate = FundUtil.calcRate(stockFund.get(0).getKey(), stockFund.get(0).getValue());
        } else {
            stockMoneyFullRate = FundUtil.calcRate(stockFund.get(0).getKey(), stockFund.get(stockFund.size() - 1).getValue());
        }
        for (int i = 0; i < totalFund.size(); i++) {
            List<FundDetail> fundDetails = totalFund.get(i);
            if (i == totalFund.size() - 1) {
                totalMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundDetails), 0, fundDetails.get(0).getYearMonth()));
            } else {
                totalMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundDetails), FundUtil.calcRate(fundDetails, totalFund.get(i + 1)), fundDetails.get(0).getYearMonth()));
            }
        }
        totalMoneyMonthRate = totalMoney.stream().map(PayTrendVO.TrendDetail::rate).reduce(0, Integer::sum);
        for (Pair<List<FundDetail>, List<FundDetail>> fundPair : stockFund) {
            stockMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundPair.getKey()), FundUtil.calcRate(fundPair.getKey(), fundPair.getValue()), fundPair.getKey().get(0).getYearMonth()));
        }
        stockMoneyMonthRate = stockMoney.stream().map(PayTrendVO.TrendDetail::rate).reduce(0, Integer::sum);
        return new PayTrendVO(totalMoneyFullRate, totalMoneyMonthRate, stockMoneyFullRate, stockMoneyMonthRate, totalMoney, stockMoney);
    }

    private List<Pair<List<FundDetail>, List<FundDetail>>> fillStockFund(Map<YearMonth, Map<LocalDate, List<FundDetail>>> ym2Date2Fund) {
        List<Pair<List<FundDetail>, List<FundDetail>>> stockFund = new ArrayList<>();
        if (ym2Date2Fund.isEmpty()) {
            return Collections.emptyList();
        }
        ym2Date2Fund.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            Map<LocalDate, List<FundDetail>> date2Details = entry.getValue();
            Map<LocalDate, List<FundDetail>> nextMonth = ym2Date2Fund.get(entry.getKey().plusMonths(1));
            if (nextMonth == null) {
                return;
            }
            stockFund.add(
                    Pair.of(
                            date2Details.entrySet().stream().max(Map.Entry.comparingByKey()).map(Map.Entry::getValue).get().stream().filter(FundDetail::isStock).toList(),
                            nextMonth.entrySet().stream().min(Map.Entry.comparingByKey()).map(Map.Entry::getValue).get().stream().filter(FundDetail::isStock).toList()
                    )
            );
        });
        return stockFund;
    }

    private List<List<FundDetail>> fillTotalFund(Map<YearMonth, Map<LocalDate, List<FundDetail>>> ym2Date2Fund) {
        List<List<FundDetail>> totalFund = new ArrayList<>();
        if (ym2Date2Fund.isEmpty()) {
            return Collections.emptyList();
        }
        YearMonth maxYearMonth = ym2Date2Fund.keySet().stream().max(YearMonth::compareTo).get();
        ym2Date2Fund.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            Map<LocalDate, List<FundDetail>> date2Details = entry.getValue();
            Set<LocalDate> localDates = date2Details.keySet();
            if (localDates.size() == 1 && entry.getKey().equals(maxYearMonth)) {
                return;
            }
            totalFund.add(date2Details.entrySet().stream().max(Map.Entry.comparingByKey()).map(Map.Entry::getValue).get());
        });
        return totalFund;
    }

    private void check(Map<YearMonth, Map<LocalDate, List<FundDetail>>> ym2Date2Fund) {
        boolean allMatch = ym2Date2Fund.values().stream().allMatch(date2Fund -> date2Fund.size() <= 2);
        if (!allMatch) {
            throw new RuntimeException("wo qiao!!! 有个月数据不对嗷！");
        }
    }
}
