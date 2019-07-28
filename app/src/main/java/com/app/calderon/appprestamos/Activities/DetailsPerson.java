package com.app.calderon.appprestamos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.app.calderon.appprestamos.Models.Details;
import com.app.calderon.appprestamos.Adapters.MyAdapterDetails;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.ABONO;
import static com.app.calderon.appprestamos.Util.Util.ADDED;
import static com.app.calderon.appprestamos.Util.Util.getCounterDetails;
import static com.app.calderon.appprestamos.Util.Util.getDate;
import static com.app.calderon.appprestamos.Util.Util.loadDataCompleted;
import static com.app.calderon.appprestamos.Util.Util.loadDataFromDetails;
import static com.app.calderon.appprestamos.Util.Util.loadDataFromPerson;
import static com.app.calderon.appprestamos.Util.Util.saveCounterDetails;
import static com.app.calderon.appprestamos.Util.Util.saveDataDetails;
import static com.app.calderon.appprestamos.Util.Util.saveDataPerson;
import static com.app.calderon.appprestamos.Util.Util.setDate;

public class DetailsPerson extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private List<Person> personList;
    private List<Details> detailsList;
    private List<Person> completedList;
    private RecyclerView recyclerView;
    private MyAdapterDetails myAdapterDetails;
    private RecyclerView.LayoutManager manager;

    private FloatingActionButton fabAbono;
    private Toolbar toolbar;

    private String nombre;
    private TextView tvFecha;
    private TextView tvCantidad;
    private TextView tvSaldo;
    private TextView tvSaldoInicai;
    private TextView tvPagos;

    private int abono = 0;

    private String fecha;
    private int pagos;
    private int plazos;
    private int cantidad;
    private int saldo;

    private TextView fechaPick;
    private EditText etAbono;

    private int position;
    private int positionId;
    private int positionDelete;
    private int counter = 0;

    private int positionList = 0;

    private SharedPreferences prefCounter;
    private SharedPreferences prefPerson;
    private SharedPreferences prefDetails;
    private SharedPreferences prefCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_person);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            positionList = bundle.getInt("positionList");//lista del recyclerView del Main
            positionId = bundle.getInt("positionID");//id unico de cada item del recyclerView del Main
        }
        getPreferences();
        sendBind();
        counter = getCounterDetails(prefCounter,myAdapterDetails,0);
        setToolbar();
        getDate(fechaPick);
        setDataFromPerson();

        sendRecyclerView();
    }

    private void getPreferences() {
        prefPerson    = getSharedPreferences("preferencesMain",MODE_PRIVATE);
        prefCounter   = getSharedPreferences("counter",MODE_PRIVATE);
        prefCounter   = getSharedPreferences("counter" + positionId,MODE_PRIVATE);
        prefDetails   = getSharedPreferences("preferencesDetails" + positionId, MODE_PRIVATE);
        prefCompleted = getSharedPreferences("preferencesCompleted",MODE_PRIVATE);
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
                        positionDelete = position;
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
        completedList = loadDataCompleted(prefCompleted,completedList);
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
        fecha = personList.get(positionList).getFechaInicial();
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
                addItem(ABONO);
                break;
            case R.id.fechaDetailsPick:
                setDate(this, fechaPick);
                break;
        }
    }

    private void addItem(int type) {
        position = detailsList.size();

        if (etAbono.getText().toString().isEmpty()) {
           etAbono.setError("Ingrese cantidad");
        } else {
            abono = Integer.parseInt(etAbono.getText().toString());
            detailsList.add(new Details(fechaPick.getText().toString(),
                    nombre,
                    abono, type
            ));
            recyclerView.scrollToPosition(position);
            myAdapterDetails.notifyItemInserted(position);
            getDate(fechaPick);
            etAbono.setText("");
            etAbono.requestFocus();
            saveDataDetails(prefDetails,detailsList, positionId);
            personList.get(positionList).setFechaFinal(fechaPick.getText().toString());
            updateSaldo(abono);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            case R.id.addFinish:
                confirmCompleted();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCompleted() {
        int finishMoney = pagos*plazos;
        int dividends = finishMoney - cantidad;
        String date = personList.get(positionList).getFechaFinal();
        if(personList.get(positionList).getAdded()!=ADDED) {
            completedList.add(personList.get(positionList));
            personList.get(positionList).setAdded(ADDED);
            Toast.makeText(this,completedList.toString(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Prestamo completado ya ha sido agregado",Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmCompleted() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsPerson.this);
        builder.setCancelable(true);
        builder.setMessage("¿Desea agregar prestamo completado");
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCompleted();
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

    private void goBack() {
        Intent intent = new Intent(DetailsPerson.this, MainActivity.class);
        /*if (send) {
            intent.putExtra("abono", saldoFinal);
            intent.putExtra("position", positionId);
            intent.putExtra("status", send);
        }*/
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    private void updateSaldo(int abono) {
        int saldoParcial = saldo;
        saldo = saldoParcial-abono;
        tvSaldo.setText(String.format(Locale.getDefault(), "$%d", saldo));
        tvSaldoInicai.setText(String.format(Locale.getDefault(), "$%d", (pagos * plazos)));
        personList.get(positionList).setSaldo(saldo);
        saveDataPerson(prefPerson,personList);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Details d = detailsList.get(positionDelete);
        switch (menuItem.getItemId()) {
            case R.id.delete:
                String m = "¿Desea borrar abono de $" + d.getCantidad() + ",\n del " + d.getFecha() + "?";
                showConfirmDeleteDialog(d,m);
                return true;
            default:
                return false;
        }
    }

    private void deleteAbono(Details d) {
        int desc = d.getCantidad();
        updateSaldo(desc);
        detailsList.remove(positionDelete);
        myAdapterDetails.notifyItemRemoved(positionDelete);
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
