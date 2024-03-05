package zzgo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhengw
 * @since 2024-03-05 14:49
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FundDetail {
    private int id;
    private CategoryEnum category;
    private LocalDate addTime;
    private Money amount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static FundDetail of(CategoryEnum category, LocalDate addTime, Money amount){
        return new FundDetail(0, category, addTime, amount, LocalDateTime.now(), LocalDateTime.now());
    }

}