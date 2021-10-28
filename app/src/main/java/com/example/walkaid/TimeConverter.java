package com.example.walkaid;

import java.util.ArrayList;
import java.util.List;

public class TimeConverter {
    private int mValue;

    public TimeConverter(int value) {
        mValue = value;
    }

    public String getFullTime() {
        int time = mValue / 3600;
        int minute = (mValue - (3600 * time)) / 60;
        int second = mValue - ((3600 * time) + (60 * minute));

        String strTime = null;
        String strMin = null;
        String strSec = null;

        if (0 < time && time < 10) {
            strTime = "0" + time;
        } else if (time >= 10) {
            strTime = String.valueOf(time);
        }

        if (minute == 0) {
            strMin = "00";
        } else if (minute > 0 && minute < 10) {
            strMin = "0" + minute;
        } else if (minute >= 10) {
            strMin = String.valueOf(minute);
        }

        if (second == 0) {
            strSec = "00";
        } else if (0 < second && second < 10) {
            strSec = "0" + second;
        } else if (second >= 10) {
            strSec = String.valueOf(second);
        }

        String returnValue;
        if (strTime != null) {
            returnValue = strTime + ":" + strMin + ":" + strSec;
        } else {
            returnValue = strMin + ":" + strSec;
        }

        return returnValue;
    }

    public List<Integer> getTimeMinute() {
        List<Integer> list = new ArrayList<>();

        int time = mValue / 3600;
        int minute = (mValue - (3600 * time)) / 60;
        int second = mValue - ((3600 * time) + (60 * minute));

        list.add(time);
        list.add(minute);
        list.add(second);

        return list;
    }
}
