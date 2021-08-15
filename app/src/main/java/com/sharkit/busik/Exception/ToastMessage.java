package com.sharkit.busik.Exception;

import android.content.Context;
import android.widget.Toast;

public class ToastMessage extends Exception{
    public ToastMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
