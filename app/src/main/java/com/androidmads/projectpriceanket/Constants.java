package com.androidmads.projectpriceanket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mushtaq on 02-10-2016.
 */
public class Constants {

    // Google Forms URL
    public static final String url = "https://docs.google.com/forms/d/e/1FAIpQLSePJJ28fBE1OG6R_IKMDHnHwLso14yCBzB1UyIzhxkg0Yp-3w/formResponse";

    // Google Form's Column ID
    public static final String nameField = "entry.1896032765";
    public static final String phoneField = "entry.1932394242";
    public static final String fioField = "entry.1634703269";
    public static final String cmsField = "entry.1210746191";
    public static final String restructField = "entry.734301943";
    public static final String projectTypeFiels = "entry.1619358544";
    public static final String siteTypeFiels = "entry.1100424901";

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
