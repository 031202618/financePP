package zzgo.domain.util;

import java.util.Collections;
import java.util.List;

public class Time {
    private Time() {
    }

    public static List<Integer> quarter2Month(int quarter) {
        return switch (quarter) {
            case 1 -> List.of(1, 2, 3);
            case 2 -> List.of(4, 5, 6);
            case 3 -> List.of(7, 8, 9);
            case 4 -> List.of(10, 11, 12);
            default -> Collections.emptyList();
        };
    }

    public static int month2Quarter(int month){
        return switch (month){
            case 1,2,3 -> 1;
            case 4,5,6 -> 2;
            case 7,8,9 -> 3;
            case 10,11,12 -> 4;
            default -> 0;
        };
    }
}
