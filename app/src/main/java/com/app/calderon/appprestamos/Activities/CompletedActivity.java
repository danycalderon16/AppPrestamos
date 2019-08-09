
package com.app.calderon.appprestamos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.calderon.appprestamos.Adapters.MyAdapterCompleted;
import com.app.calderon.appprestamos.Adapters.MyAdapterPerson;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.goMain;
import static com.app.calderon.appprestamos.Util.Util.loadDataCompleted;
import static com.app.calderon.appprestamos.Util.Util.saveDataCompleted;

public class CompletedActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private List<Person> completedList;
    private RecyclerView recyclerView;
    private MyAdapterCompleted myAdapterCompleted;
    private RecyclerView.LayoutManager manager;

    private Toolbar toolbar;
    private TextView total;
    private SlidrInterface  slidr;

    private SharedPreferences prefCompleted;

    private int c = 0;
    private int positionDelete = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        setToolbar();
        prefCompleted = getSharedPreferences("preferencesCompleted",MODE_PRIVATE);
        completedList = loadDataCompleted(prefCompleted,completedList);
        setTotal();
        setRecyclerView();
        slidr = Slidr.attach(this);

    }

    private void setTotal() {
        int money = 0;

        int size = completedList.size();
        if(size!=0) {
            for (int i = 0; i < size; i++) {
                money += ((completedList.get(i).getPagos() * completedList.get(i).getPlazos())-
                            completedList.get(i).getQuantity());
            }
        }
        total = findViewById(R.id.textViewTotal);
        total.setText(String.format(Locale.getDefault(),"$%d",money));
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c++;
                if(c>5){
                    completedList.clear();
                    saveDataCompleted(prefCompleted,completedList);
                    goMain(CompletedActivity.this);
                }
            }
        });
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbarCompleted);
        toolbar.setTitle("Completados");
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewCompleted);

        manager = new LinearLayoutManager(this);
        myAdapterCompleted = new MyAdapterCompleted(completedList, this,
                new MyAdapterCompleted.OnItemEventListener() {
                    @Override
                    public void onMoreClicked(Person completedPerson, int position, View v) {
                        positionDelete = position;
                        PopupMenu popupMenu = new PopupMenu(CompletedActivity.this,v);
                        popupMenu.setOnMenuItemClickListener(CompletedActivity.this);
                        popupMenu.inflate(R.menu.menu_main);
                        popupMenu.show();

                    }
                });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapterCompleted);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goMain(this);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goMain(this);
        super.onBackPressed();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.completedList.get(info.position).getName());
        inflater.inflate(R.menu.menu_main,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete:
                this.deleteCompleted(info.position);
                return  true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteCompleted(int position) {
        completedList.remove(position);
        myAdapterCompleted.notifyDataSetChanged();
        Toast.makeText(this, "Eliminación exitosa", Toast.LENGTH_SHORT).show();
        setTotal();
        saveDataCompleted(prefCompleted,completedList);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.delete:
                showConfirmDeleteDialog();
        }
        return false;
    }
    public void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompletedActivity.this);
        builder.setCancelable(true);
        builder.setMessage("¿Desea borrar prestamo completado?");
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCompleted(positionDelete);
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
