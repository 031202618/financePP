package zzgo.app.vo;

import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record PayAnalysisVo(
        Map<String, String> module2Money,
        List<StockInfo> stockList
){
    public record StockInfo(
            String category,
            String amount,
            String comment
    ){
    }
}
