package com.majapahit.imagine.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rafy on 11/12/2017.
 */

public class Utility {
    public static String getTimestamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        return format;
    }
}
