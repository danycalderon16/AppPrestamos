package com.app.calderon.appprestamos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.ABONO;
import static com.app.calderon.appprestamos.Util.ATRASO;
import static com.app.calderon.appprestamos.Util.getCounterDetails;
import static com.app.calderon.appprestamos.Util.getCounterId;
import static com.app.calderon.appprestamos.Util.getDate;
import static com.app.calderon.appprestamos.Util.loadDataFromDetails;
import static com.app.calderon.appprestamos.Util.loadDataFromPerson;
import static com.app.calderon.appprestamos.Util.saveCounterDetails;
import static com.app.calderon.appprestamos.Util.saveDataDetails;
import static com.app.calderon.appprestamos.Util.setDate;

public class DetailsPerson extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


    private List<Person> personList;
    private List<Details> detailsList;
    private RecyclerView recyclerView;
    private MyAdapterDetails myAdapterDetails;
    private RecyclerView.LayoutManager manager;

    private FloatingActionButton fabAbono;
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
    private int positionId;
    private int counter = 0;
    private boolean send = false;

    private int positionList = 0;

    private final int DESCONTAR = 333;
    private final int SUMAR = 334;

    private SharedPreferences prefCounter;
    private SharedPreferences prefPerson;
    private SharedPreferences prefDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_person);

        getPreferences();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            positionList = bundle.getInt("positionList");//lista del recyclerView del Main
        }
        sendBind();
        counter = getCounterDetails(prefCounter,myAdapterDetails,0);
        setToolbar();
        getDate(fechaPick);
        setDataFromPerson();

        sendRecyclerView();
    }

    private void getPreferences() {
        prefPerson  = getSharedPreferences("preferencesMain",MODE_PRIVATE);
        prefCounter = getSharedPreferences("counter",MODE_PRIVATE);
        positionId  = getCounterId(prefCounter,0);
        prefCounter = getSharedPreferences("counter" + positionId,MODE_PRIVATE);
        prefDetails = getSharedPreferences("preferencesDetails" + positionId, MODE_PRIVATE);
    }

    private void setDataFromPerson() {
        tvFecha.setText(fecha);
        tvCantidad.setText(String.format(Locale.getDefault(), "$%d", cantidad));
        tvSaldo.setText(String.format(Locale.getDefault(), "$%d", saldo));
        tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
        tvSaldoInicai.setText(String.format(Locale.getDefault(), "$%d", (pagos * plazos)));
    }

    private void setToolbar() {
        toolbar.setTitle(nombre);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void sendRecyclerView() {
        detailsList = loadDataFromDetails(prefDetails,detailsList, positionId);

        manager = new LinearLayoutManager(this);
        myAdapterDetails = new MyAdapterDetails(detailsList, this, positionId,
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

    private void sendBind() {

        personList = loadDataFromPerson(prefPerson,personList);
        toolbar = findViewById(R.id.toolbar);
        fabAbono = findViewById(R.id.fabAbono);
        tvFecha = findViewById(R.id.fechaDetails);
        tvCantidad = findViewById(R.id.cantidadDetails);
        tvSaldo = findViewById(R.id.saldodDetails);
        tvSaldoInicai = findViewById(R.id.saldoInicial);
        fechaPick = findViewById(R.id.fechaDetailsPick);
        etAbono = findViewById(R.id.abono);
        recyclerView = findViewById(R.id.rvDetails);
        tvPagos = findViewById(R.id.pagosDetails);

        nombre = personList.get(positionList).getName();
        fecha = personList.get(positionList).getFecha();
        pagos = personList.get(positionList).getPagos();
        plazos = personList.get(positionList).getPlazos();
        cantidad = personList.get(positionList).getQuantity();
        saldo = personList.get(positionList).getSaldo();

        fabAbono.setOnClickListener(this);
        fechaPick.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabAbono:
                send = true;
                addItem(ABONO, DESCONTAR);
                //editSaldo();
                break;
            case R.id.fechaDetailsPick:
                setDate(this, fechaPick);
                break;
        }
    }

    private void addItem(int type, int operation) {
        position = detailsList.size();

        if (etAbono.getText().toString().isEmpty()) {
           etAbono.setError("Ingrese cantidad");
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
            saveDataDetails(prefDetails,detailsList, positionId);
            updateSaldo(abonoAux, operation);
            counter++;
            tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
            saveCounterDetails(prefCounter,positionId,counter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBack() {
        Intent intent = new Intent(DetailsPerson.this, MainActivity.class);
        if (send) {
            intent.putExtra("abono", saldoFinal);
            intent.putExtra("position", positionId);
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
        saveDataDetails(prefDetails,detailsList, positionId);
        Toast.makeText(this, "Borrado exitoso", Toast.LENGTH_SHORT).show();
        counter--;
        tvPagos.setText(String.format(Locale.getDefault(), "%d/%d", counter, plazos));
        saveCounterDetails(prefCounter,positionId,counter);
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
}
