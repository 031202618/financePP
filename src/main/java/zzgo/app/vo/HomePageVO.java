package zzgo.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zzgo.domain.HomePageInfo;

import java.time.LocalDate;

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
}
