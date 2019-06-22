package com.app.calderon.appprestamos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.List;

import static com.app.calderon.appprestamos.Util.saveDataPerson;

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

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            saldo = itemView.findViewById(R.id.saldo);
            date = itemView.findViewById(R.id.date);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Person person) {
            name.setText(person.getName());
            quantity.setText("$"+person.getQuantity());
            saldo.setText("$"+person.getSaldo());
            date.setText(person.getFecha());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(person,getAdapterPosition());
                }
            });
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
            builder.setCancelable(true);
            builder.setMessage("¿Desea borrar prestamo de "+people.get(getAdapterPosition()).getName()+"?");
            builder.setPositiveButton("Sí",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            people.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            saveDataPerson(people,context);
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
