package com.sharkit.busik.Validation;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.sharkit.busik.Exception.ToastMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationFlight {

    private TextInputEditText startCountry, finishCountry, startCity, finishCity, priceCargo, pricePassenger, startDate, finishDate;;
    private Context getContext;

    public ValidationFlight(TextInputEditText startCountry, TextInputEditText finishCountry, TextInputEditText startCity,
                            TextInputEditText finishCity, TextInputEditText startDate, TextInputEditText finishDate, TextInputEditText priceCargo, TextInputEditText pricePassenger, Context getContext) {
        this.startCountry = startCountry;
        this.finishCountry = finishCountry;
        this.startCity = startCity;
        this.finishCity = finishCity;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.getContext = getContext;
        this.priceCargo = priceCargo;
        this.pricePassenger = pricePassenger;
    }

    public boolean isValidFlight(){

        if (TextUtils.isEmpty(startCountry.getText())){
            try {
                throw new ToastMessage("Введите страну отправления", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (Validator(startCountry.getText().toString())){
            try {
                throw new ToastMessage("Страна не должна иметь сиволов и цифр", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }


        if (TextUtils.isEmpty(finishCountry.getText())){
            try {
                throw new ToastMessage("Введите Страну прибытия", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (Validator(finishCountry.getText().toString())){
            try {
                throw new ToastMessage("Страна не должна иметь символов и цифр", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }


        if (TextUtils.isEmpty(startCity.getText())){
            try {
                throw new ToastMessage("Введите город отправления", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (Validator(startCity.getText().toString())){
            try {
                throw new ToastMessage("Город не должен иметь символов и цифр", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(finishCity.getText())){
            try {
                throw new ToastMessage("Введите город прибытия", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (Validator(finishCity.getText().toString())){
            try {
                throw new ToastMessage("Город не должен иметь символов и цифр", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(priceCargo.getText())){
            try {
                throw new ToastMessage("Введите цену за кг груза", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }

        if (TextUtils.isEmpty(pricePassenger.getText())){
            try {
                throw new ToastMessage("Введите цену за пассажира", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }


        if (TextUtils.isEmpty(startDate.getText())){
            try {
                throw new ToastMessage("Введите дату отправления", getContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return false;
        }
        if (TextUtils.isEmpty(finishDate.getText())){
            try {
                throw new ToastMessage("Введите дату прибытия", getContext);
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
        Matcher hasNum = num.matcher(s);
        Matcher hasSigns = sign.matcher(s);
        return (hasSigns.find() || hasNum.find());
    }
    private boolean isDate(String s){
        Pattern num = Pattern.compile("[A-Z], [a-z], [а-я], [А-Я]");
        Matcher hasNum = num.matcher(s);
        return (hasNum.find());
    }
}
