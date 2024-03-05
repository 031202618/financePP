package zzgo.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhengw
 * @since 2024-03-05 14:55
 */
@AllArgsConstructor
@Getter
public enum CategoryEnum {
    ALL(0, "全部"),
    JJ(1, "基金"),
    GP(2, "股票"),
    ZQ(3, "债券"),
    HJ(4, "黄金"),
    ;
    private final int id;
    private final String desc;
}
