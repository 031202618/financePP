package zzgo.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@SQLDelete(sql = "UPDATE fund_detail SET delete_time = NOW() WHERE id = ? ")
@Where(clause = "delete_time is null")
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

    public FundDetail toDomain() {
        return new FundDetail(id, CategoryEnum.of(id), addTime, new Money(amount), createTime, updateTime);
    }
}
