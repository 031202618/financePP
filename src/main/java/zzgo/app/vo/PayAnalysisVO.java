package zzgo.app.vo;

import java.util.List;
import java.util.Map;

public record PayAnalysisVO(
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
