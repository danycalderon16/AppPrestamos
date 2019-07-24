package com.app.calderon.appprestamos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static List<Person> loadDataFromPerson(SharedPreferences preferences,List<Person> personList) {
        Gson gson = new Gson();
        String json = preferences.getString("task list main", null);
        Type type = new TypeToken<ArrayList<Person>>() {
        }.getType();
        personList = gson.fromJson(json, type);

        if (personList == null) {
            personList = new ArrayList<>();
        }

        return  personList;
    }

    public static List<Details> loadDataFromDetails(SharedPreferences preferences, List<Details> detailsList,int positionId) {
        Gson gson = new Gson();
        String json = preferences.getString("task list details" + positionId, null);
        Type type = new TypeToken<ArrayList<Details>>() {
        }.getType();
        detailsList = gson.fromJson(json, type);

        if (detailsList == null) {
            detailsList = new ArrayList<>();
        }

        return detailsList;
    }

    public static void saveDataPerson(SharedPreferences preferences, List<Person> personList) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(personList);
        editor.putString("task list main", json);
        editor.apply();
    }

    public static void saveDataDetails(SharedPreferences preferences, List<Details> details, int positionId) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(details);
        editor.putString("task list details" + positionId, json);
        editor.apply();
    }

    /*----------------------------------------------------------------------*/
    // CONTADORES DE IRREPETIBLES DE LOS ITEMS DEL RECYCLER VIEW PRESTAMOS
    public static  void saveCounterId(SharedPreferences pref,int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("count", value);
        editor.apply();
    }

    public static int getCounterId(SharedPreferences pref,int value) {
        return pref.getInt("count", value);
    }
    /*----------------------------------------------------------------------*/


    /*----------------------------------------------------------------------*/
    // CONTADORES DE IRREPETIBLES DE LOS ITEMS DEL RECYCLER VIEW DE LOS ABONOS

    public static int getCounterDetails(SharedPreferences pref,MyAdapterDetails myAdapterDetails,int value) {
        int i = 0;
        if (myAdapterDetails != null) i = myAdapterDetails.getItemCount();
        return pref.getInt("counter" + value, 0) + i;
    }

    public static void saveCounterDetails(SharedPreferences preferences, int positionId, int counter) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter" + positionId, counter);
        editor.apply();
    }
    /*----------------------------------------------------------------------*/

}
