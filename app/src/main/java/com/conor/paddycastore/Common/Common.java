package com.conor.paddycastore.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.conor.paddycastore.Model.User;

public class Common {

    public static User currentUser;
    public static String currentStock;

    public static String getCurrentStock() {
        return currentStock;
    }

    public static void setCurrentStock(String currentStock) {
        Common.currentStock = currentStock;
    }

    public static String topicName = "News";
    public static String venueSelected = "";
    public static final int PICK_IMAGE_REQUEST = 71;

    public static int drinkSizeChoice = -1; // -1 = none chosen; 0 = Pint; 1 = Bottle
    // Added to try and get the type of drink to display in the cart view
//    public static int drinkSizeCart = -1; // -1 = none chosen; 0 = Pint; 1 = Bottle

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    public static final String baseUrl = "https://maps.googleapis.com/";

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Being made";
        else if (status.equals("2"))
            return "Ready For Collection";
        else
            return "Collected";
    }

    public static Boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
}
