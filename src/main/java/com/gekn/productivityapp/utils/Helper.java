package com.gekn.productivityapp.utils;

import java.time.Duration;
import java.util.Locale;

public class Helper {

    public static String formatDuration(long totalTimeMillis) {
        Duration totalDuration = Duration.ofMillis(totalTimeMillis);
        return String.format(Locale.ENGLISH,
                "%02d:%02d:%02d",
                totalDuration.toHours(),
                totalDuration.toMinutesPart(),
                totalDuration.toSecondsPart());
    }

}
