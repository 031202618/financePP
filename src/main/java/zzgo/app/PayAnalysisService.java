package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.app.vo.PayAnalysisVo;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayAnalysisService {

    private final FundDetailService fundDetailService;

    public PayAnalysisVo getPayAnalysisVo(int year, int month) {
        List<FundDetail> fundDetails = fundDetailService.getAll().stream()
                .sorted(Comparator.comparing(FundDetail::getAddTime))
                .filter(fundDetail -> {
                    LocalDate addTime = fundDetail.getAddTime();
                    return addTime.getYear() == year && addTime.getMonthValue() == month;
                })
                .toList();
        Map<String, String> module2Money = fundDetails.stream().collect(Collectors.groupingBy(x -> x.getCategory().getModule())).entrySet()
                .stream()
                .map(entry -> {
                    Money money = entry.getValue().stream().map(FundDetail::getAmount).reduce(Money::add).orElse(Money.zero());
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), money.toString());
                }).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        return new PayAnalysisVo(
                module2Money,
                fundDetails.stream().filter(x -> x.getCategory().isStock())
                        .map(x -> new PayAnalysisVo.StockInfo(x.getCategory().getDesc(), x.getAmount().toAmount(), x.getComment()))
                        .toList()
        );
    }
}
