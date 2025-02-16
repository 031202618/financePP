package zzgo.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import zzgo.domain.HomePageInfo;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author zhengw
 * @since 2024-09-21 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePageVO {
    private HomePageInfo homePageInfo;
    private String totalAmount;
    private String stockAmount;
    private double stockRate;
    private LocalDate startFundDate;
    private LocalDate endFundDate;
    /**
     * {年份: {年初理财资金, 年末理财资金, 整体收益}}
     */
    private Map<Integer, Triple<Money, Money, Money>> year2FundSummary;
}
