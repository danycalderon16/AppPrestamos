package com.app.calderon.appprestamos.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.calderon.appprestamos.Models.Details;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.ABONO;
import static com.app.calderon.appprestamos.Util.Util.ATRASO;

public class MyAdapterDetails extends RecyclerView.Adapter<MyAdapterDetails.ViewHolder> {

    private List<Details> detailsList;
    private Activity activity;
    private int pos;
    private OnItemEventListener listener;

    public MyAdapterDetails(List<Details> detailsList,
                            Activity activity,
                            int pos,
                            OnItemEventListener listener) {
        this.detailsList = detailsList;
        this.activity = activity;
        this.pos = pos;
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_details_person, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(detailsList.get(position));

    }

    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fecha;
        public TextView cantidad;
        public CardView cardView;
        public TextView type;


        public ViewHolder(View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.dateDetails);
            cantidad = itemView.findViewById(R.id.quantityDetails);
            cardView = itemView.findViewById(R.id.cantidadDetails);
            type = itemView.findViewById(R.id.txtType);
        }

        public void bind(final Details details) {
            String f[] = details.getFecha().split("/");
            fecha.setText( f[0] +" de "+ lagerMonth(details) +" de " + f[2]);
            cantidad.setText(String.format(Locale.getDefault(), "$%d", +details.getCantidad()));
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    listener.onMoreClicked(details, getAdapterPosition(), view);
                }
            });

            if(details.getType()==ATRASO){
                type.setText("Atraso");
                cantidad.setText(String.format(Locale.getDefault(), "+ $%d", +details.getCantidad()));
                cantidad.setTextColor(activity.getResources().getColor(R.color.red_primary_dark));
            }
            if(details.getType()==ABONO){
                type.setText("Abono");
                cantidad.setText(String.format(Locale.getDefault(), "- $%d", +details.getCantidad()));
                cantidad.setTextColor(activity.getResources().getColor(R.color.green_primary));
            }
        }
    }

    private String lagerMonth(Details d) {
        String c ="";
        String f[] = d.getFecha().split("/");
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


    public interface OnItemEventListener {
        void onMoreClicked(Details details, int position, View view);
    }

}
