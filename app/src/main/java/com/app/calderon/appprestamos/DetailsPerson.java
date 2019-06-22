package com.app.calderon.appprestamos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.ABONO;
import static com.app.calderon.appprestamos.Util.ATRASO;
import static com.app.calderon.appprestamos.Util.getDate;
import static com.app.calderon.appprestamos.Util.saveDataDetails;
import static com.app.calderon.appprestamos.Util.setDate;

public class DetailsPerson extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


    private List<Details> detailsList;
    private RecyclerView recyclerView;
    private MyAdapterDetails myAdapterDetails;
    private RecyclerView.LayoutManager manager;

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private CoordinatorLayout coordy;

    private String nombre;
    private TextView tvFecha;
    private TextView tvCantidad;
    private TextView tvSaldo;
    private TextView tvSaldoInicai;
    private TextView tvPagos;

    private int abonoAux = 0;
    private int abonoSend = 0;
    private int saldoFinal = 0;

    private String fecha;
    private int pagos;
    private int plazos;
    private int cantidad;
    private int saldo;

    private TextView fechaPick;
    private EditText etAbono;

    private int position;
    private int positionD;
    private int positionRela;
    private int counter = 0;
    private boolean send = false;

    private final int DESCONTAR = 333;
    private final int SUMAR = 334;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_person);

        sendBind();

        pref = getSharedPreferences("counter" + positionRela,
                Context.MODE_PRIVATE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nombre = bundle.getString("nombre");
            fecha = bundle.getString("fecha");
            cantidad = bundle.getInt("cantidad");
            saldo = bundle.getInt("saldo");
            pagos = bundle.getInt("pagos");
            plazos = bundle.getInt("plazos");
            positionRela = bundle.getInt("position");
        }
        counter = getCounterSaved();
        setToolbar();
        getDate(fechaPick);
        //nombre.setText(nombreS);
        tvFecha.setText(fecha);
        tvCantidad.setText(String.format(Locale.getDefault(), "$%d", cantidad));
        tvSaldo.setText(String.format(Locale.getDefault(), "$%d", saldo));
        tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
        tvSaldoInicai.setText(String.format(Locale.getDefault(), "$%d", (pagos * plazos)));

        sendRecyclerView();
    }

    private void setToolbar() {
        toolbar.setTitle(nombre);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void sendRecyclerView() {
        loadData();

        manager = new LinearLayoutManager(this);
        myAdapterDetails = new MyAdapterDetails(detailsList, this, positionRela,
                new MyAdapterDetails.OnItemEventListener() {
                    @Override
                    public void onMoreClicked(Details details, int position, View v) {
                        positionD = position;
                        PopupMenu popup = new PopupMenu(DetailsPerson.this, v);
                        popup.setOnMenuItemClickListener(DetailsPerson.this);
                        popup.inflate(R.menu.menu_main);
                        popup.show();
                    }
                });
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnCreateContextMenuListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapterDetails);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "preferencesDetails" + positionRela, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list details" + positionRela, null);
        Type type = new TypeToken<ArrayList<Details>>() {
        }.getType();
        detailsList = gson.fromJson(json, type);

        if (detailsList == null) {
            detailsList = new ArrayList<>();
        }
    }

    private void sendBind() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fabDetails);
        //nombre = findViewById(R.id.txtPersonaDetails);
        tvFecha = findViewById(R.id.fechaDetails);
        tvCantidad = findViewById(R.id.cantidadDetails);
        tvSaldo = findViewById(R.id.saldodDetails);
        tvSaldoInicai = findViewById(R.id.saldoInicial);
        coordy = findViewById(R.id.coodyDetails);
        fechaPick = findViewById(R.id.fechaDetailsPick);
        etAbono = findViewById(R.id.abono);
        recyclerView = findViewById(R.id.rvDetails);
        tvPagos = findViewById(R.id.pagosDetails);

        fab.setOnClickListener(this);
        fechaPick.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabDetails:
                send = true;
                addItem(ABONO, DESCONTAR);
                break;
            case R.id.fechaDetailsPick:
                setDate(this, fechaPick);
                break;
        }
    }

    private void addItem(int type, int operation) {
        position = detailsList.size();

        if (etAbono.getText().toString().isEmpty()) {
            Snackbar.make(coordy, "Ingrese la cantidad ", Snackbar.LENGTH_SHORT).show();
        } else {
            abonoAux = Integer.parseInt(etAbono.getText().toString());
            detailsList.add(new Details(fechaPick.getText().toString(),
                    nombre,
                    abonoAux, type
            ));
            recyclerView.scrollToPosition(position);
            myAdapterDetails.notifyItemInserted(position);
            getDate(fechaPick);
            etAbono.setText("");
            etAbono.requestFocus();
            saveDataDetails(detailsList,this,positionRela);
            updateSaldo(abonoAux, operation);
            counter++;
            tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
            saveCounter();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            case R.id.addAtraso:
                addItem(ATRASO, SUMAR);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBack() {
        Intent intent = new Intent(DetailsPerson.this, MainActivity.class);
        if (send) {
            intent.putExtra("abono", saldoFinal);
            intent.putExtra("position", positionRela);
            intent.putExtra("status", send);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updateSaldo(int ab, int opc) {
        String s = tvSaldo.getText().toString();
        int sa = Integer.parseInt(s.substring(1));
        if (opc == DESCONTAR) saldoFinal = sa - ab;
        if (opc == SUMAR) saldoFinal = sa + ab;
        tvSaldo.setText("$" + saldoFinal);
        send = true;
        abonoSend = saldoFinal;
        tvSaldoInicai.setText(String.format(Locale.getDefault(), "$%d", (pagos * plazos)));
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Details d = detailsList.get(positionD);
        switch (menuItem.getItemId()) {
            case R.id.delete:
                String m="";
                if(d.getType()==ABONO){ m =  "¿Desea borrar abono de $" + d.getCantidad() + ",\n del " + d.getFecha() + "?";}
                if(d.getType()==ATRASO){ m =  "¿Desea borrar atraso de $" + d.getCantidad() + ",\n del " + d.getFecha() + "?";}
                showConfirmDeleteDialog(d,m);
                return true;
            default:
                return false;
        }
    }

    private void deleteAbono(Details d) {
        int desc = d.getCantidad();
        if(d.getType() == ATRASO)  updateSaldo(desc, DESCONTAR);
        if(d.getType() == ABONO)  updateSaldo(desc, SUMAR);
        detailsList.remove(positionD);
        myAdapterDetails.notifyItemRemoved(positionD);
        saveDataDetails(detailsList,this,positionRela);
        Toast.makeText(this, "Borrado exitoso", Toast.LENGTH_SHORT).show();
        counter--;
        tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
        saveCounter();
    }

    public void showConfirmDeleteDialog(final Details d,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsPerson.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAbono(d);
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

    private int getCounterSaved() {
        int i = 0;
        if (myAdapterDetails != null) i = myAdapterDetails.getItemCount();
        return pref.getInt("counter" + positionRela, counter) + i;
    }

    private void saveCounter() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("counter" + positionRela, counter);
        editor.apply();
    }

}
