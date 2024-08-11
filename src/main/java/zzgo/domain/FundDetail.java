package zzgo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

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
    private String comment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int bindId;

    public static FundDetail of(CategoryEnum category, LocalDate addTime, Money amount, String comment, int version) {
        return new FundDetail(0, category, addTime, amount, comment, LocalDateTime.now(), LocalDateTime.now(), version);
    }

    public YearMonth getYearMonth() {
        return YearMonth.of(addTime.getYear(), addTime.getMonth());
    }

    public boolean isStock() {
        return category.isStock();
    }

    public boolean isSnapshot() {
        return bindId != 0;
    }

    public void snapshot() {
        this.bindId = this.id;
        this.id = 0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void copyOld(FundDetail old) {
        this.id = old.getId();
        this.createTime = old.getCreateTime();
        this.updateTime = LocalDateTime.now();
    }
}