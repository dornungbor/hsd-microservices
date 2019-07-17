package com.dornu.hsd.server.domain.constants;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneOffset.UTC);
}
