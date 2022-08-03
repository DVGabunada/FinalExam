package com.example.midterms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class PipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipe);
        btnConfirmListenerMethod();

        // TODO Milestone A: get SharedPreference on night mode and change false to variable
        initializeNightMode(false);
    }

    // TODO Milestone A: implement night mode
    private void initializeNightMode(boolean night) {

    }

    private void btnConfirmListenerMethod() {
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Intent outIntent = new Intent();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Milestone B-1: use the outIntent and store the chosen brand and the chosen diameter as extras
                //  and after which, use setResult with ACTION_OK and the intent as arguments
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

    }
}