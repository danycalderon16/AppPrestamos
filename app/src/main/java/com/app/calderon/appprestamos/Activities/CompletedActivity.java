
package com.app.calderon.appprestamos.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.calderon.appprestamos.Adapters.MyAdapterCompleted;
import com.app.calderon.appprestamos.Adapters.MyAdapterPerson;
import com.app.calderon.appprestamos.Models.Person;
import com.app.calderon.appprestamos.R;

import java.util.List;

import static com.app.calderon.appprestamos.Util.Util.loadDataCompleted;

public class CompletedActivity extends AppCompatActivity {

    private List<Person> completedList;
    private RecyclerView recyclerView;
    private MyAdapterCompleted myAdapterCompleted;
    private RecyclerView.LayoutManager manager;

    private SharedPreferences prefCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        prefCompleted = getSharedPreferences("preferencesCompleted",MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerViewCompleted);

        completedList = loadDataCompleted(prefCompleted,completedList);
        manager = new LinearLayoutManager(this);
        myAdapterCompleted = new MyAdapterCompleted(completedList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnCreateContextMenuListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapterCompleted);
    }
}
