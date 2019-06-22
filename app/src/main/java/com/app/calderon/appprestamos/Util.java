package com.app.calderon.appprestamos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Util {

    public static int dia;
    public static int mes;
    public static int year;
    public static String mesS[] = {"ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic"};
    public static Calendar c = Calendar.getInstance();
    public final static int  ABONO = 87;
    public final static int  ATRASO = 88;


    public static void getDate(TextView textView) {
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        textView.setText(String.format(Locale.getDefault(),"%d/%s/%d",dia, mesS[mes] , year));
    }

    public static void setDate(Context context, final TextView textView){if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int motnhOfYear, int dayOfMonth) {
                textView.setText(String.format(Locale.getDefault(),"%d/%s/%d",dayOfMonth, mesS[motnhOfYear] , year));
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }}

    public static void saveDataPerson(List<Person> personList,Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencesMain",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(personList);
        editor.putString("task list main", json);
        editor.apply();
    }

    public static void saveDataDetails(List<Details> details, Activity activity,int positionRela) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("preferencesDetails" + positionRela, activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(details);
        editor.putString("task list details" + positionRela, json);
        editor.apply();
    }
}
