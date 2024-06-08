package zzgo.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author zhengw
 * @since 2024-03-05 14:55
 */
@AllArgsConstructor
@Getter
public enum CategoryEnum {
    JJ(1001, "基金"),
    GP(1002, "股票"),
    ZQ(1003, "债券"),
    HJ(1004, "黄金"),
    LQKZ(1005, "理财空置"),
    LQKZ_GZ(1006, "理财空置-工资"),
    SH(2001, "生活"),
    TB(3001, "玩乐"),
    GJJ(4001, "公积金");
    private final int id;
    private final String desc;

    public static CategoryEnum of(int id) {
        return Arrays.stream(CategoryEnum.values()).filter(x -> x.getId() == id).findAny().get();
    }

    public boolean isStock() {
        return (id / 1000) == 1;
    }

    public String getModule() {
        return switch (id / 1000) {
            case 1 -> "理财";
            case 2 -> "生活";
            case 3 -> "玩乐";
            case 4 -> "公积金";
            default -> "";
        };
    }
}
