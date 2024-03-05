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
    JJ(1, "基金"),
    GP(2, "股票"),
    ZQ(3, "债券"),
    HJ(4, "黄金"),
    SH(5, "生活"),
    TB(6, "玩乐")
    ;
    private final int id;
    private final String desc;

    public static CategoryEnum of(int id) {
        return Arrays.stream(CategoryEnum.values()).filter(x -> x.getId() == id).findAny().get();
    }
}
