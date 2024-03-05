package zzgo.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zzgo.domain.FundDetail;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhengw
 * @since 2024-03-05 15:33
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "fund_detail")
public class FundDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int category;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate addTime;
    @Column
    private int amount;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateTime;

    public static FundDetailEntity of(FundDetail fundDetail) {
        return new FundDetailEntity(0, fundDetail.getCategory().getId(), fundDetail.getAddTime(), fundDetail.getAmount().amount(), fundDetail.getCreateTime(), fundDetail.getUpdateTime());
    }
}
