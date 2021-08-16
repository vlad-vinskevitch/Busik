package com.sharkit.busik.Validation;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputEditText;
import com.sharkit.busik.Exception.ToastMessage;

public class ValidationAuthorisation {
    private Context context;
    private TextInputEditText email, password;

    public ValidationAuthorisation(TextInputEditText email, TextInputEditText password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
    }
    public boolean isValidInput(){
        if (isValidEmail(email.getText())){
            try {
                throw new ToastMessage("Введите корректный формат почты", context  );
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        return true;
    }

    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
