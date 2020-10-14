package com.sof.testnews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Constants {
    public static final String url = "https://newsapi.org/v2/";
    public static final String urlTopHeadlines =
            "https://newsapi.org/v2/top-headlines?q=apple&apiKey=e65ee0938a2a43ebb15923b48faed18d/";
    public static final String urlEverything =
            "https://newsapi.org/v2/everything?q=apple&apiKey=e65ee0938a2a43ebb15923b48faed18d/";
    public static final  String NEWS_KEY = "NEWS_KEY";
    public static final  String LAST_PAGE_KEY = "LAST_PAGE_KEY";
    public static final  String TOTAL_PAGES_KEY = "TOTAL_PAGES_KEY";
    public static final  String CURRENT_PAGE_KEY = "CURRENT_PAGE_KEY";
    public static String getDate(String utcDate) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
        java.util.Date dateObj = null;
        if (utcDate == null) {
            utcDate = "";
        }
        try {
            dateObj = sdf.parse(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = utcDate;
        if (dateObj != null)
            date = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").format(dateObj);
        return date;
    }


}
