package com.app.calderon.appprestamos.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.BIWEEKLY;
import static com.app.calderon.appprestamos.Util.Util.WEEKLY;
import static com.app.calderon.appprestamos.Util.Util.saveDataPerson;

public class MyAdapterPerson extends RecyclerView.Adapter<MyAdapterPerson.ViewHolder>{

    private List<Person> people;
    private Activity activity;
    private Context context;
    private OnItemClickListener listener;

    public MyAdapterPerson(List<Person> people, Activity activity,Context context, OnItemClickListener listener) {
        this.people = people;
        this.activity = activity;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.content_main, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(people.get(position));
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView name;
        public TextView quantity;
        public TextView saldo;
        public TextView date;
        public TextView items;
        public ImageView circle;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            saldo = itemView.findViewById(R.id.saldo);
            date = itemView.findViewById(R.id.date);
            items = itemView.findViewById(R.id.itemCounter);
            circle = itemView.findViewById(R.id.payMethod);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Person person) {
            name.setText(person.getName());
            quantity.setText(String.format(Locale.getDefault(), "$%d",person.getQuantity()));
            saldo.setText(String.format(Locale.getDefault(),"$%d",person.getSaldo()));
            date.setText(person.getFechaInicial());
            if(people!=null){
                items.setText(String.format(Locale.getDefault(),"%d/%d",getAdapterPosition()+1,people.size()));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(person,getAdapterPosition());
                }
            });
            if(person.getPayment() == WEEKLY){
                circle.setBackground(context.getDrawable(R.drawable.circle_shape_weekly));
            }
            if(person.getPayment() == BIWEEKLY){
                circle.setBackground(context.getDrawable(R.drawable.circle_shape_biweekly));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Person person = people.get(this.getAdapterPosition());

            menu.setHeaderTitle(person.getName());
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.menu_main,menu);
            for (int i=0;i<menu.size();i++){
                menu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    showConfirmDeleteDiaglog();
                    return true;
                default:
                    return false;
            }
        }

        public void showConfirmDeleteDiaglog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final SharedPreferences prefPerson = context.getSharedPreferences("preferencesMain",context.MODE_PRIVATE);
            builder.setCancelable(true);
            builder.setMessage("¿Desea borrar prestamo de "+people.get(getAdapterPosition()).getName()+"?");
            builder.setPositiveButton("Sí",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            people.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            saveDataPerson(prefPerson,people);
                            Toast.makeText(context,"Borrado exitoso",Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Person person,int position);
    }
}
