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

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalDateTime recordTime;
    @Column
    private long amount;
    @Column
    private String comment;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateTime;
    @Column
    private int bindId;

    public static FundDetailEntity of(FundDetail fundDetail) {
        return new FundDetailEntity(fundDetail.getId(), fundDetail.getCategory().getId(), LocalDateTime.of(fundDetail.getAddTime(), LocalTime.MIDNIGHT), fundDetail.getAmount().amount(), fundDetail.getComment(), fundDetail.getCreateTime(), fundDetail.getUpdateTime(), fundDetail.getBindId());
    }

    public FundDetail toDomain() {
        return new FundDetail(id, CategoryEnum.of(category), recordTime.toLocalDate(), new Money(amount), comment, createTime, updateTime, bindId);
    }
}
