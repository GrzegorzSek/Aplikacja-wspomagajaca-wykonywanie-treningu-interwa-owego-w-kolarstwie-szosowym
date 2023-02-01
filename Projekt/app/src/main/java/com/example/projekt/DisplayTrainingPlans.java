package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayTrainingPlans extends AppCompatActivity {

    private static final String TAG = "DispalyTrainingPlans";

    DatabaseHelper mDatabaseHelper;

    private ListView t_plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_training_plans);
        t_plans = (ListView) findViewById(R.id.training_plans);
        mDatabaseHelper = new DatabaseHelper(this);

        display();
    }

    private void display() {
        Log.d(TAG, "Displaying data in the ListView");

        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){
            listData.add(data.getString(0));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        t_plans.setAdapter(adapter);

        t_plans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "item clicked: " + name);

                Cursor data = mDatabaseHelper.getItemId(name);
                int itemId = -1;
                while(data.moveToNext()){
                    itemId = data.getInt(0);
                }
                if(itemId > -1){
                    Log.d(TAG, "The id is: " + itemId);
                    Intent intent = new Intent(DisplayTrainingPlans.this, PlanOptions.class);
                    intent.putExtra("id", itemId);
                    startActivity(intent);
                }else{
                    Log.d(TAG, "wrong id");
                }
            }
        });
    }
}