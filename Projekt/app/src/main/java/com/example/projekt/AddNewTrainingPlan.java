package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewTrainingPlan extends AppCompatActivity {

    private Context context;
    private final int duration = Toast.LENGTH_SHORT;

    DatabaseHelper mDatabaseHelper;

    private static final String TAG = "AddNewTrainingPlan";

    private Button addDataBtn;
    private EditText name;
    private EditText warm_up;
    private EditText low_intst;
    private EditText high_intst;
    private EditText sets;
    private EditText rest;
    private EditText cool_down;
    private EditText cycles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_training_plan);

        addDataBtn = (Button) findViewById(R.id.addDataBtn);
        name = (EditText) findViewById(R.id.name);
        warm_up = (EditText) findViewById(R.id.warm_up);
        low_intst = (EditText) findViewById(R.id.low_intst);
        high_intst = (EditText) findViewById(R.id.high_intst);
        sets = (EditText) findViewById(R.id.sets);
        rest = (EditText) findViewById(R.id.rest);
        cool_down = (EditText) findViewById(R.id.cool_down);
        cycles = (EditText) findViewById(R.id.cycles);

        mDatabaseHelper = new DatabaseHelper(this);


        addDataBtn.setOnClickListener(new View.OnClickListener() {
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

                if(name.length() !=0 && warm_up.length() !=0 && low_intst.length() !=0 && high_intst.length() !=0 && sets.length() !=0 && rest.length() !=0 && cool_down.length() !=0 && cycles.length() !=0){
                    addData(name_txt, warm_up_txt, low_intst_txt, high_intst_txt, sets_txt, rest_txt, cool_down_txt, cycles_txt);
                    name.setText("");
                    warm_up.setText("");
                    low_intst.setText("");
                    high_intst.setText("");
                    sets.setText("");
                    rest.setText("");
                    cool_down.setText("");
                    cycles.setText("");
                }else{
                    Toast.makeText(AddNewTrainingPlan.this, "Fill in all fields", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "blad");
                }
            }
        });
    }

    public void addData(String name, String warm_up, String low_intst, String high_intst, String sets, String rest, String cool_down, String cycles) {
        boolean insertData = mDatabaseHelper.addData(name, warm_up, low_intst, high_intst, sets, rest, cool_down, cycles);

        if(insertData){
            Toast.makeText(AddNewTrainingPlan.this, "Training plan added successfully", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Training plan added successfully");
        }else{
            Toast.makeText(AddNewTrainingPlan.this, "Something went wrong", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Something went wrong");
        }
    }

//    private void toastMessage(String message){
//        Toast.makeText(context, message, duration).show();
//    }
}