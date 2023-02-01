package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlanOptions extends AppCompatActivity {

    private static final String TAG = "PlanOptions";

    DatabaseHelper mDatabaseHelper;

    private Button edit;
    private Button run;
    private Button delete;

    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_options);

        mDatabaseHelper = new DatabaseHelper(this);

        edit = (Button) findViewById(R.id.edit);
        run = (Button) findViewById(R.id.run);
        delete = (Button) findViewById(R.id.delete);

        Intent receivedIntent = getIntent();
        selectedId = receivedIntent.getIntExtra("id", -1);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanOptions.this, EditPlan.class);
                intent.putExtra("id", selectedId);
                startActivity(intent);
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanOptions.this, RunActivity.class);
                intent.putExtra("id", selectedId);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteData(selectedId);
                Intent intent = new Intent(PlanOptions.this, DisplayTrainingPlans.class);
                startActivity(intent);
            }
        });
    }
}