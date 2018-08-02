package com.example.android.pocintenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class OtherActivity extends AppCompatActivity {

    private TextView textViewData;
    private String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        initComponents();
        initIntent();
        fillData();
    }

    private void initComponents() {
        textViewData = findViewById(R.id.textFillData);
    }

    private void initIntent() {
        name = getIntent().getStringExtra("name");
    }

    private void fillData() {
        textViewData.setText(name);
    }
}
