package zzgo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-08-11 16:05
 */
@Getter
@AllArgsConstructor
public enum WinLevel {
    D(0, "⛤级[0%~20%)"),
    C(20, "⛤⛤级[20%~40%)"),
    B(40, "⛤⛤⛤级[40%~60%)"),
    B_PLUS(60, "⛤⛤⛤⛤级[60%~80%)"),
    A(80, "⛤⛤⛤⛤⛤级[80%~100%)"),
    S(100, "最强水平, 站上收益之巅!");

    private final int lowPercent;
    private final String desc;

    public static String getLevel(double beatPercent) {
        List<WinLevel> list = Arrays.stream(WinLevel.values()).sorted(Comparator.comparing(WinLevel::getLowPercent).reversed()).toList();
        for (WinLevel winLevel : list) {
            if (beatPercent >= winLevel.getLowPercent()) {
                return winLevel.getDesc();
            }
        }
        return D.getDesc();
    }
}
