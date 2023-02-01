package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);
    }

    public void add_new_training_plan_on_click(View view) {
//        Toast.makeText(MainActivity.this, "Fill in all fields", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, AddNewTrainingPlan.class);
        startActivity(intent);
    }

    public void display_training_plans_on_click(View view) {
        Intent intent = new Intent(MainActivity.this, DisplayTrainingPlans.class);
        startActivity(intent);
    }
}