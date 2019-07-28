package com.app.calderon.appprestamos.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.calderon.appprestamos.Models.Details;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

public class MyAdapterCompleted extends  RecyclerView.Adapter<MyAdapterCompleted.ViewHolder>{

    private List<Person> completedList;
    private Activity activity;

    public MyAdapterCompleted(List<Person> completedList, Activity activity) {
        this.completedList = completedList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_completed,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(completedList.get(position));
    }

    @Override
    public int getItemCount() {
        return completedList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView quantity;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCompleted);
            quantity = itemView.findViewById(R.id.quantityCompleted);
            date = itemView.findViewById(R.id.dateCompleted);
        }

        public void bind(Person person) {
            name.setText(person.getName());
            String f[] = person.getFechaFinal().split("/");
            date.setText(f[0] +" de "+ lagerMonth(person) +" de " + f[2]);
            int cantidad = person.getPagos()*person.getPlazos();
            quantity.setText(String.format(Locale.getDefault(),
                    "$%d",cantidad));
        }
    }

    private String lagerMonth(Person person) {
        String c ="";
        String f[] = person.getFechaFinal().split("/");
        switch (f[1].toString()) {
            case "ene":
                c ="Enero";
                break;
            case "feb":
                c= "Febrero";
                break;
            case "mar":
                c= "Marzo";
                break;
            case "abr":
                c= "Abril";
                break;
            case "may":
                c= "Mayo";
                break;
            case "jun":
                c= "Junio";
                break;
            case "jul":
                c= "Julio";
                break;
            case "ago":
                c= "Agosto";
                break;
            case "sep":
                c= "Septiembre";
                break;
            case "oct":
                c= "Octubre";
                break;
            case "nov":
                c= "Noviembre";
                break;
            case "dic":
                c= "Diciembre";
                break;
            default:
                return "";
        }
        return  c;
    }


}
