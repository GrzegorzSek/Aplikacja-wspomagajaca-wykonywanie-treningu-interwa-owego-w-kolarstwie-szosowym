package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class EditPlan extends AppCompatActivity {

//    private static final String TABLE_NAME = "traning_plan";
//    private static final String ID = "training_plan_id";
//    private static final String PLAN_TRAINING_NAME = "name";
//    private static final String WARM_UP = "warm_up"; // warm up time
//    private static final String LOW_INTST = "low"; // Low intensity time
//    private static final String HIGH_INTST = "high"; // High intensity time
//    private static final String SETS = "sets"; // number of sets (low/high)
//    private static final String REST = "rest"; // time between cycles
//    private static final String COOL_DOWN = "cool_down"; // time after training
//    private static final String CYCLES = "cycles"; // number of sets repetitions

    DatabaseHelper mDatabaseHelper;

    private static final String TAG = "EditPlan";

    private Button saveDataBtn;
    private EditText name;
    private EditText warm_up;
    private EditText low_intst;
    private EditText high_intst;
    private EditText sets;
    private EditText rest;
    private EditText cool_down;
    private EditText cycles;

    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);

        saveDataBtn = (Button) findViewById(R.id.saveDataBtn);
        name = (EditText) findViewById(R.id.name);
        warm_up = (EditText) findViewById(R.id.warm_up);
        low_intst = (EditText) findViewById(R.id.low_intst);
        high_intst = (EditText) findViewById(R.id.high_intst);
        sets = (EditText) findViewById(R.id.sets);
        rest = (EditText) findViewById(R.id.rest);
        cool_down = (EditText) findViewById(R.id.cool_down);
        cycles = (EditText) findViewById(R.id.cycles);

        mDatabaseHelper = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();
        selectedId = receivedIntent.getIntExtra("id", -1);

        display();

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_txt = name.getText().toString();
                String warm_up_txt = warm_up.getText().toString();
                String low_intst_txt = low_intst.getText().toString();
                String high_intst_txt = high_intst.getText().toString();
                String sets_txt = sets.getText().toString();
                String rest_txt = rest.getText().toString();
                String cool_down_txt = cool_down.getText().toString();
                String cycles_txt = cycles.getText().toString();

                mDatabaseHelper.updateData(selectedId, name_txt, warm_up_txt, low_intst_txt, high_intst_txt, sets_txt, rest_txt, cool_down_txt, cycles_txt);
                Intent intent = new Intent(EditPlan.this, DisplayTrainingPlans.class);
                startActivity(intent);
                Toast.makeText(EditPlan.this, "Training plan saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void display(){
        Cursor data = mDatabaseHelper.getFullData(selectedId);
        mDatabaseHelper.getFullData(selectedId);

        while(data.moveToNext()){
            name.setText(data.getString(0));
            warm_up.setText(data.getString(1));
            low_intst.setText(data.getString(2));
            high_intst.setText(data.getString(3));
            sets.setText(data.getString(4));
            rest.setText(data.getString(5));
            cool_down.setText(data.getString(6));
            cycles.setText(data.getString(7));
        }
    }
}