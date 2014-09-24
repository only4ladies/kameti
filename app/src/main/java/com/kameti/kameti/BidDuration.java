package com.kameti.kameti;

import android.util.Log;

import java.util.GregorianCalendar;

public class BidDuration {

    private long[] auctionTime;
    private long auctionDuration;

    public BidDuration(String startDate, String startTime, String endTime, int auctionCount) {
        String[] startDateArray = startDate.split("-");
        int year = Integer.valueOf(startDateArray[0]);
        int month = Integer.valueOf(startDateArray[1]);
        int date = Integer.valueOf(startDateArray[2]);
        String[] startTimeArray = startTime.split(":");
        int start_hour = Integer.valueOf(startTimeArray[0]);
        int start_minute = Integer.valueOf(startTimeArray[1]);
        int start_second = Integer.valueOf(startTimeArray[2]);
        String[] endTimeArray = endTime.split(":");
        int end_hour = Integer.valueOf(endTimeArray[0]);
        int end_minute = Integer.valueOf(endTimeArray[1]);
        int end_second = Integer.valueOf(endTimeArray[2]);

        GregorianCalendar firstBidStart = new GregorianCalendar();
        firstBidStart.set(year, month-1, date, start_hour, start_minute, start_second);
        GregorianCalendar firstBidEnd = new GregorianCalendar();
        firstBidEnd.set(year, month-1, date, end_hour, end_minute, end_second);
        auctionDuration = firstBidEnd.getTimeInMillis() - firstBidStart.getTimeInMillis();

        auctionTime = new long[auctionCount];
        for(int i=0; i<auctionCount; i++){
            auctionTime[i] = firstBidStart.getTimeInMillis();
            firstBidStart.add(GregorianCalendar.MONTH, 1);
        }
    }
    public boolean contains(long time) {
        for(int i=0; i<auctionTime.length; i++){
            if(time >= auctionTime[i] && time <= auctionTime[i] + auctionDuration){
                return true;
            }
        }
        return false;
    }
}