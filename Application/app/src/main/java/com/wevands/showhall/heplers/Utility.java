package com.wevands.showhall.heplers;

import android.content.Context;
import android.util.DisplayMetrics;
/*
    Reference : https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
 */
public class Utility {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
