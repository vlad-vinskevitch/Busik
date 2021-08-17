package com.sharkit.busik.Exception;

import android.content.Context;

public class NoConnectInternet extends Exception{
    public NoConnectInternet( Context context) {
        try {
            throw new ToastMessage("Проверте подключение к интернету", context);
        } catch (ToastMessage toastMessage) {
            toastMessage.printStackTrace();
        }
    }
}
