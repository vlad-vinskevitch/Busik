package com.sharkit.busik.Validation;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputEditText;
import com.sharkit.busik.Exception.ToastMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegistration {
    private TextInputEditText name, last_name, country, city, password, phone, email, accept_pass;
    private Context context;

    public ValidationRegistration(TextInputEditText name, TextInputEditText last_name, TextInputEditText country, TextInputEditText city,
                                  TextInputEditText password, TextInputEditText phone, TextInputEditText email, TextInputEditText accept_pass, Context context) {
        this.accept_pass = accept_pass;
        this.name = name;
        this.last_name = last_name;
        this.country = country;
        this.city = city;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.context = context;
    }


    public boolean isEmptyInput()  {
        if (TextUtils.isEmpty(name.getText())){
            try {
                throw new ToastMessage("Введите имя", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (name.getText().length() < 2){
            try {
                throw new ToastMessage("Имя должно иметь больше 1 буквы", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (name.getText().length() > 70){
            try {
                throw new ToastMessage("Имя не должно иметь больше 70 букв", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
        }
        if (Validator(name.getText().toString().trim())){
            try {
                throw new ToastMessage("Имя не должно иметь символов или цифр", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(last_name.getText())){
            try {
                throw new ToastMessage("Введите фамилию", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (last_name.getText().length() < 2){
            try {
                throw new ToastMessage("Фамилия не должна иметь больше 1 буквы", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (last_name.getText().length() > 70){
            try {
                throw new ToastMessage("Фамилия не должна иметь больше 70 букв", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
        }


        if (Validator(last_name.getText().toString().trim())){
            try {
                throw new ToastMessage("Фамилия не должна иметь символов или цифр", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(country.getText())) {
            try {
                throw new ToastMessage("Введите страну", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (Validator(country.getText().toString().trim())){
            try {
                throw new ToastMessage("Страна не должна иметь символов или цифр", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(city.getText())){
            try {
                throw new ToastMessage("Введите город", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;

        }

        if (Validator(city.getText().toString().trim())){
            try {
                throw new ToastMessage("Город не должен иметь символов или цифр", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (TextUtils.isEmpty(phone.getText())){
            try {
                throw new ToastMessage("Введите номер телефона", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (phone.getText().length() != 12){
            try {
                throw new ToastMessage("Введите корректный номер телефона", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (TextUtils.isEmpty(email.getText())){
            try {
                throw new ToastMessage("Введите почту", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (!isValidEmail(email.getText())){
            try {
                throw new ToastMessage("Введите корректный формат почты", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (TextUtils.isEmpty(password.getText())){
            try {
                throw new ToastMessage("Введите пароль", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (password.getText().length() < 6){
            try {
                throw new ToastMessage("Пароль должен иметь больше 5 символов", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (ValidatorPass(password.getText().toString().trim())){
            try {
                throw new ToastMessage("Пароль не должен иметь символов и кириллици", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (TextUtils.isEmpty(accept_pass.getText())){
            try {
                throw new ToastMessage("Подтвердите пароль", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (!accept_pass.getText().toString().trim().equals(password.getText().toString().trim())){
            try {
                throw new ToastMessage("Пароли не совпадают", context);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        return true;

    }
    private boolean Validator(String s){
        Pattern num = Pattern.compile("[0-9]");
        Pattern sign = Pattern.compile("[!@#$:%&*()_+=|<>?{}\\[\\]~×÷/€£¥₴^\";,`]°•○●□■♤♡◇♧☆▪¤《》¡¿,.]");
        Pattern space = Pattern.compile(" ");
        Matcher hasSpace = space.matcher(s);
        Matcher hasNum = num.matcher(s);
        Matcher hasSigns = sign.matcher(s);

        return (hasSigns.find() || hasNum.find() || hasSpace.find());
    }

    private boolean ValidatorPass(String s){
        Pattern cyrillic = Pattern.compile("[а-яА-Я]");
        Pattern sign = Pattern.compile("[!@#$:%&*()_+=|<>?{}\\[\\]~×÷/€£¥₴^\";,`]°•○●□■♤♡◇♧☆▪¤《》¡¿,.]");
        Pattern space = Pattern.compile(" ");
        Matcher hasSign = sign.matcher(s);
        Matcher hasCyrillic = cyrillic.matcher(s);
        Matcher hasSpace = space.matcher(s);
        return  (hasCyrillic.find() || hasSpace.find() || hasSign.find());
    }

    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
