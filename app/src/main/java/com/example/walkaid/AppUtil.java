package com.example.walkaid;

import java.util.LinkedList;

public class AppUtil {
    long sum;
    int size;
    LinkedList<Long> list;

    /** Initialize your data structure here. */
    public AppUtil(int size) {
        this.list = new LinkedList<>();
        this.size = size;
    }

    public double next(long val) {
        sum += val;
        list.offer(val);

        if(list.size()<=size){
            return sum/list.size();
        }

        sum -= list.poll();

        return sum/size;
    }
}
