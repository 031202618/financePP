package zzgo.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import zzgo.domain.FundDetail;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author zhengw
 * @since 2024-03-05 15:33
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql = "UPDATE fund_detail_snapshot SET delete_time = NOW() WHERE id = ? ")
@Where(clause = "delete_time is null")
@Entity
@Table(name = "fund_detail_snapshot")
public class FundDetailSnapshotEntity {
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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime snapshotTime;

    public static FundDetailSnapshotEntity of(FundDetail fundDetail) {
        return new FundDetailSnapshotEntity(0, fundDetail.getCategory().getId(), LocalDateTime.of(fundDetail.getAddTime(), LocalTime.MIDNIGHT), fundDetail.getAmount().amount(), fundDetail.getComment(), fundDetail.getCreateTime(), fundDetail.getUpdateTime(), fundDetail.getBindId(), LocalDateTime.now());
    }
}
