/*
 * Decompiled with CFR 0.152.
 */
package com.fugoworld.backend.service;

import java.util.Calendar;
import java.util.Date;

public class DateService {
    public static Date getDateInTwoDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return calendar.getTime();
    }

    public static boolean isDateInPast(Date date) {
        return date.before(new Date());
    }
}
