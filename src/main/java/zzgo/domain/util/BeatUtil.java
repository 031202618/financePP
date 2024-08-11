package zzgo.domain.util;

import java.util.List;

/**
 * @author zhengw
 * @since 2024-08-11 16:27
 */
public class BeatUtil {
    public static final double beatPercent(double curRate, List<Double> totalRate) {
        int totalSize = totalRate.size() - 1;
        long lessSize = totalRate.stream().filter(rate -> rate.compareTo(curRate) < 0).count();
        return (double) lessSize * 100 / totalSize;
    }
}
