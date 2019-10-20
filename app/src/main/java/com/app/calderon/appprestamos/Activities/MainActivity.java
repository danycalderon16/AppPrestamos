package com.app.calderon.appprestamos.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.calderon.appprestamos.Adapters.MyAdapterPerson;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.BIWEEKLY;
import static com.app.calderon.appprestamos.Util.Util.NO_ADDED;
import static com.app.calderon.appprestamos.Util.Util.getCounterId;
import static com.app.calderon.appprestamos.Util.Util.getDate;
import static com.app.calderon.appprestamos.Util.Util.loadDataFromPerson;
import static com.app.calderon.appprestamos.Util.Util.saveCounterId;
import static com.app.calderon.appprestamos.Util.Util.saveDataPerson;
import static com.app.calderon.appprestamos.Util.Util.setDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Person> personList;
    private RecyclerView recyclerView;
    private MyAdapterPerson myAdapterPerson;
    private RecyclerView.LayoutManager manager;

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private CoordinatorLayout coordy;

    private TextInputLayout nombre;
    private TextInputLayout cantidad;
    private TextInputLayout plazos;
    private TextInputLayout pagos;
    private TextView fecha;

    private SharedPreferences prefCounter;
    private SharedPreferences prefPerson;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPreferences();
        sendBind();
        getDate(fecha);
        setSupportActionBar(toolbar);
        sendRecycler();
    }

    private void setPreferences() {
        prefCounter = getSharedPreferences("counter",MODE_PRIVATE);
        prefPerson  = getSharedPreferences("preferencesMain",MODE_PRIVATE);
    }

    private void sendRecycler() {
        personList = loadDataFromPerson(prefPerson,personList);

        manager = new LinearLayoutManager(this);
        myAdapterPerson = new MyAdapterPerson(personList,this,this,
                new MyAdapterPerson.OnItemClickListener() {
            @Override
            public void onItemClick(Person person, int position) {
                Intent intent = new Intent(MainActivity.this, DetailsPerson.class);
                intent.putExtra("positionList",position);
                intent.putExtra("positionID",person.getPositionID());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapterPerson);
    }

    private void sendBind() {
        count = getCounterId(prefCounter,0);

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.addPerson);
        recyclerView = findViewById(R.id.rv);
        coordy = findViewById(R.id.coordy);
        nombre = findViewById(R.id.nombre);
        cantidad = findViewById(R.id.cantidad);
        plazos = findViewById(R.id.plazos);
        fecha = findViewById(R.id.fecha);
        pagos = findViewById(R.id.pagos);

        fecha.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showInfoDialog();
                return false;
            }
        });
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater  = getLayoutInflater();
        View v = inflater.inflate(R.layout.info_layout,null);

        int recuperar = 0;
        int ganar = 0;

        int infoQuantity = 0;
        int infoSaldo = 0;
        int saldoInicial = 0;

        TextView infoRecuperar = v.findViewById(R.id.txtRecuperar);
        TextView infoGanar     = v.findViewById(R.id.txtGanar);
        TextView infoTotal     = v.findViewById(R.id.txtTotal);

        for(int i = 0; i < myAdapterPerson.getItemCount();i++){
            int abonado = 0;
            infoQuantity = personList.get(i).getQuantity();
            saldoInicial = personList.get(i).getPagos() *  personList.get(i).getPlazos();
            infoSaldo = personList.get(i).getSaldo();

            abonado = saldoInicial - infoSaldo;

            if(infoQuantity>abonado){
                recuperar += (infoQuantity-abonado);
                ganar += (saldoInicial-infoQuantity);
            }else{
                ganar += (saldoInicial-abonado);
            }
        }

        infoRecuperar.setText(String.format(Locale.getDefault(),"$%d",recuperar));
        infoGanar.setText(String.format(Locale.getDefault(),"$%d",ganar));
        infoTotal.setText(String.format(Locale.getDefault(),"$%d",(ganar+recuperar)));

        builder.setCancelable(true);
        builder.create();
        builder.show();

        builder.setView(v);
        builder.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        myAdapterPerson.notifyDataSetChanged();
        saveDataPerson(prefPerson,personList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPerson:
                addPerson();
                break;
            case R.id.fecha:
                setDate(this, fecha);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_completed,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_completed:
                Intent intent = new Intent(this, CompletedActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateEditText(TextInputLayout textInputLayout) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        if (text.isEmpty()) {
            textInputLayout.setError("Campo requerido");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
            }else{
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    private void addPerson() {
        int position = personList.size();
        int saldo = 0;
        if (!validateEditText(nombre) | !validateEditText(cantidad) |
                !validateEditText(plazos) | !validateEditText(pagos))
            return;
        saldo = Integer.parseInt(plazos.getEditText().getText().toString()) *
                Integer.parseInt(pagos.getEditText().getText().toString());
        personList.add(new Person(nombre.getEditText().getText().toString(),
                Integer.parseInt(cantidad.getEditText().getText().toString()),
                Integer.parseInt(plazos.getEditText().getText().toString()),
                Integer.parseInt(pagos.getEditText().getText().toString()),
                saldo,
                fecha.getText().toString(),
                fecha.getText().toString(),
                count,
                NO_ADDED,BIWEEKLY));
        count++;

        saveCounterId(prefCounter,count);

        recyclerView.scrollToPosition(position);
        myAdapterPerson.notifyItemInserted(position);
        nombre.getEditText().setText("");
        cantidad.getEditText().setText("");
        pagos.getEditText().setText("");
        plazos.getEditText().setText("");
        getDate(fecha);
        nombre.requestFocus();
        saveDataPerson(prefPerson,personList);
        Snackbar.make(coordy,"Prestamo agregado",Snackbar.LENGTH_SHORT).show();

    }
}
