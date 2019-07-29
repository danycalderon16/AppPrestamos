
package com.app.calderon.appprestamos.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.calderon.appprestamos.Adapters.MyAdapterCompleted;
import com.app.calderon.appprestamos.Adapters.MyAdapterPerson;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;
import java.util.Locale;

import static com.app.calderon.appprestamos.Util.Util.loadDataCompleted;

public class CompletedActivity extends AppCompatActivity {

    private List<Person> completedList;
    private RecyclerView recyclerView;
    private MyAdapterCompleted myAdapterCompleted;
    private RecyclerView.LayoutManager manager;

    private Toolbar toolbar;
    private TextView total;

    private SharedPreferences prefCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        setToolbar();
        prefCompleted = getSharedPreferences("preferencesCompleted",MODE_PRIVATE);
        completedList = loadDataCompleted(prefCompleted,completedList);
        setTotal();
        setRecyclerView();

    }

    private void setTotal() {
        int money = 0;
        int size = completedList.size();
        if(size!=0) {
            for (int i = 0; i <= size; i++) {
                money += (completedList.get(i).getPagos() * completedList.get(i).getPlazos());
            }
        }
        total = findViewById(R.id.textViewTotal);
        total.setText(String.format(Locale.getDefault(),"$%d",money));
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
        myAdapterCompleted = new MyAdapterCompleted(completedList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnCreateContextMenuListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapterCompleted);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
