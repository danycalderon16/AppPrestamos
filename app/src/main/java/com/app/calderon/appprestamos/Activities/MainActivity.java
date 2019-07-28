package com.app.calderon.appprestamos.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.calderon.appprestamos.Adapters.MyAdapterPerson;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;

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

    private int positionRec;
    private int nuevoSaldo;
    private boolean statusRec;

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
/*
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nuevoSaldo = bundle.getInt("abono");
            positionRec = bundle.getInt("position");
            statusRec = bundle.getBoolean("status");
        }*/
    }

    private void setPreferences() {
        prefCounter = getSharedPreferences("counter",MODE_PRIVATE);
        prefPerson  = getSharedPreferences("preferencesMain",MODE_PRIVATE);
    }

    private void sendRecycler() {
        personList = loadDataFromPerson(prefPerson,personList);

        manager = new LinearLayoutManager(this);
        myAdapterPerson = new MyAdapterPerson(personList,
                this,
                this,
                new MyAdapterPerson.OnItemClickListener() {
            @Override
            public void onItemClick(Person person, int position) {
                Intent intent = new Intent(MainActivity.this, DetailsPerson.class);
                intent.putExtra("positionList",position);
                intent.putExtra("positionID",person.getPositionID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (statusRec) {
            for (int i = 0; i < personList.size(); i++) {
                if (positionRec == personList.get(i).getPositionID()) {
                    personList.get(i).setSaldo(nuevoSaldo);
                }
            }
        }*/
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

    private boolean validateEditText(TextInputLayout textInputLayout) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        if (text.isEmpty()) {
            textInputLayout.setError("Campo requerido");
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
                NO_ADDED));
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
