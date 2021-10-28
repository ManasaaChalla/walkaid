package com.example.walkaid;

public class StrUtils {
    public static String getPaceStr(long pace){
        String paceStr = "";
        int min = (int)(pace / 60);
        int sec = (int)(pace - min * 60);
        if(sec < 10){
            paceStr = min+"\'0"+sec+"\"";
        }else{
            paceStr = min + "\'" + sec + "\"";
        }
        return paceStr;
    }
}