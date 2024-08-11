package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import zzgo.app.vo.PayTrendVO;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.WinLevel;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.BeatUtil;
import zzgo.domain.util.FundUtil;
import zzgo.domain.util.Money;
import zzgo.domain.util.Time;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayTrendService {

    private final FundDetailService fundDetailService;

    public PayTrendVO getPayTrendVo(YearMonth startYM, YearMonth endYM) {
        PayTrendVO fullPayTrend = getPayTrendVO(YearMonth.of(2000, 1), YearMonth.of(3000, 1));
        List<Double> stockRateList = fullPayTrend.stockMoney().stream().map(PayTrendVO.TrendDetail::rate).toList();
        List<Double> totalRateList = fullPayTrend.totalMoney().stream().map(PayTrendVO.TrendDetail::rate).toList();
        return getPayTrendVO(startYM, endYM, stockRateList, totalRateList);
    }

    private PayTrendVO getPayTrendVO(YearMonth startYM, YearMonth endYM) {
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

    private PayTrendVO getPayTrendVO(YearMonth startYM, YearMonth endYM, List<Double> stockRateList, List<Double> totalRateList) {
        PayTrendVO payTrendVO = getPayTrendVO(startYM, endYM);
        List<PayTrendVO.TrendDetail> totalMoney = payTrendVO.totalMoney();
        List<PayTrendVO.TrendDetail> stockMoney = payTrendVO.stockMoney();

        for (int i = 0; i < totalMoney.size(); i++) {
            PayTrendVO.TrendDetail trendDetail = totalMoney.get(i);
            double beatPercent = BeatUtil.beatPercent(trendDetail.rate(), totalRateList);
            totalMoney.set(i, new PayTrendVO.TrendDetail(trendDetail.money(), trendDetail.stackMoney(), trendDetail.rate(), trendDetail.date(), WinLevel.getLevel(beatPercent), beatPercent));
        }

        for (int i = 0; i < stockMoney.size(); i++) {
            PayTrendVO.TrendDetail trendDetail = stockMoney.get(i);
            double beatPercent = BeatUtil.beatPercent(trendDetail.rate(), stockRateList);
            stockMoney.set(i, new PayTrendVO.TrendDetail(trendDetail.money(), trendDetail.stackMoney(), trendDetail.rate(), trendDetail.date(), WinLevel.getLevel(beatPercent), beatPercent));
        }
        return payTrendVO;
    }

    private PayTrendVO buildPayTrendVO(List<List<FundDetail>> totalFund, List<Pair<List<FundDetail>, List<FundDetail>>> stockFund) {
        double totalMoneyFullRate;
        double totalMoneyFullYearRate;
        double totalMoneyMonthRate;
        double totalMoneyMonthYearRate;
        double stockMoneyFullRate;
        double stockMoneyFullYearRate;
        double stockMoneyMonthRate;
        double stockMoneyMonthYearRate;
        long totalMoneyGapDay = 1;
        long stockMoneyGapDay = 1;
        List<PayTrendVO.TrendDetail> totalMoney = new ArrayList<>();
        List<PayTrendVO.TrendDetail> stockMoney = new ArrayList<>();
        if (totalFund.size() < 2) {
            totalMoneyFullRate = 0;
            totalMoneyFullYearRate = 0;
        } else {
            List<FundDetail> firstFund = totalFund.get(0);
            List<FundDetail> lastFund = totalFund.get(totalFund.size() - 1);
            totalMoneyFullRate = FundUtil.calcRate(firstFund, lastFund);
            totalMoneyGapDay = Time.getGapDays(firstFund.get(0).getAddTime(), lastFund.get(0).getAddTime());
            totalMoneyFullYearRate = (totalMoneyFullRate / (totalMoneyGapDay)) * 365;
        }
        if (stockFund.isEmpty()) {
            stockMoneyFullRate = 0;
            stockMoneyFullYearRate = 0;
        } else if (stockFund.size() == 1) {
            stockMoneyFullRate = FundUtil.calcRate(stockFund.get(0).getKey(), stockFund.get(0).getValue());
            stockMoneyGapDay = Time.getGapDays(stockFund.get(0).getKey().get(0).getAddTime(), stockFund.get(0).getValue().get(0).getAddTime());
            stockMoneyFullYearRate = (stockMoneyFullRate / stockMoneyGapDay) * 365;
        } else {
            stockMoneyFullRate = FundUtil.calcRate(stockFund.get(0).getKey(), stockFund.get(stockFund.size() - 1).getValue());
            stockMoneyGapDay = Time.getGapDays(stockFund.get(0).getKey().get(0).getAddTime(), stockFund.get(stockFund.size() - 1).getValue().get(0).getAddTime());
            stockMoneyFullYearRate = (stockMoneyFullRate / stockMoneyGapDay) * 365;
        }
        for (int i = 0; i < totalFund.size(); i++) {
            List<FundDetail> fundDetails = totalFund.get(i);
            if (i == totalFund.size() - 1) {
                totalMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundDetails), Money.zero(), 0, fundDetails.get(0).getAddTime(), "", 0));
            } else {
                totalMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundDetails), FundUtil.getTotalMoney(totalFund.get(i + 1)).subtract(FundUtil.getTotalMoney(fundDetails)), FundUtil.calcRate(fundDetails, totalFund.get(i + 1)), fundDetails.get(0).getAddTime(), "", 0));
            }
        }
        totalMoneyMonthRate = totalMoney.stream().map(PayTrendVO.TrendDetail::rate).reduce(0.0, Double::sum);
        totalMoneyMonthYearRate = (totalMoneyMonthRate / totalMoneyGapDay) * 365;
        for (Pair<List<FundDetail>, List<FundDetail>> fundPair : stockFund) {
            stockMoney.add(new PayTrendVO.TrendDetail(FundUtil.getTotalMoney(fundPair.getKey()), FundUtil.getTotalMoney(fundPair.getValue()).subtract(FundUtil.getTotalMoney(fundPair.getKey())), FundUtil.calcRate(fundPair.getKey(), fundPair.getValue()), fundPair.getKey().get(0).getAddTime(), "", 0));
        }
        stockMoneyMonthRate = stockMoney.stream().map(PayTrendVO.TrendDetail::rate).reduce(0.0, Double::sum);
        stockMoneyMonthYearRate = (stockMoneyMonthRate / stockMoneyGapDay) * 365;
        return new PayTrendVO(
                totalMoneyFullRate,
                totalMoneyFullYearRate,
                totalMoneyMonthRate,
                totalMoneyMonthYearRate,
                stockMoneyFullRate,
                stockMoneyFullYearRate,
                stockMoneyMonthRate,
                stockMoneyMonthYearRate,
                totalMoney,
                stockMoney);
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
                            nextMonth.entrySet().stream().min(Map.Entry.comparingByKey()).map(Map.Entry::getValue).get().stream().filter(FundDetail::isStock).filter(fundDetail -> fundDetail.getCategory() != CategoryEnum.LQKZ_GZ).toList()
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
//        YearMonth maxYearMonth = ym2Date2Fund.keySet().stream().max(YearMonth::compareTo).get();
        ym2Date2Fund.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            Map<LocalDate, List<FundDetail>> date2Details = entry.getValue();
//            Set<LocalDate> localDates = date2Details.keySet();
//            if (localDates.size() == 1 && entry.getKey().equals(maxYearMonth)) {
//                return;
//            }
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
