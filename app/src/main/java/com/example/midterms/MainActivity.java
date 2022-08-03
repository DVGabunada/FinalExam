package com.example.midterms;

import static com.example.midterms.Bill.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

// TODO Milestone 3-1: implement LoaderManager.LoaderCallbacks<Cursor>
public class MainActivity extends AppCompatActivity implements BillDialogFragment.ErrorDialogListener {
    ArrayList<Bill> bills;
    BillsAdapter billsAdapter;
    int month;
    int last_consumption;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("vincepref", MODE_PRIVATE);
        editor = preferences.edit();
        bills = new ArrayList<>();
        month = 1;
        last_consumption = 0;
        btnCalculateListenerMethod();
        setHistoryAdapter();
        nightModeListenerMethod();
        btnPipeListenerMethod();

        // TODO Milestone 3-3: use initLoader() here
    }

    private void btnPipeListenerMethod() {
        Intent intent = new Intent(this, PipeActivity.class);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent outIntent = result.getData();
                            // TODO Milestone B-2: handle outIntent containing extras: brand and diameter
                            //  and set text of tvPipe to Brand/Diameter (example: Arad/0.5)
                        }
                    }
                });

        ImageButton btnPipe = findViewById(R.id.btnPipe);
        btnPipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialNightMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Milestone 3-4: use restartLoader() here
    }

    private void initialNightMode() {
        Switch swNight = findViewById(R.id.swNight);
        int[] textviews = {R.id.swNight, R.id.tvTitle, R.id.rbRegular, R.id.rbBasic, R.id.rbPremium, R.id.tvLblPackage, R.id.tvLblPipe, R.id.tvPipe, R.id.tvLblPrev, R.id.tvLblNew, R.id.tvLblHistory, R.id.tvLblBill, R.id.etResult, R.id.etPrev, R.id.etNew};
        ConstraintLayout clMain = findViewById(R.id.clMain);
        boolean night = preferences.getBoolean("night", false);
        if (night) {
            clMain.setBackgroundColor(Color.BLACK);
            swNight.setChecked(true);
            for (int res : textviews) {
                TextView view1 = findViewById(res);
                view1.setTextColor(Color.WHITE);
            }
        } else {
            swNight.setChecked(false);
            clMain.setBackgroundColor(Color.WHITE);
            for (int res : textviews) {
                TextView view1 = findViewById(res);
                view1.setTextColor(Color.BLACK);
            }
        }
    }

    // Milestone A: Use Day-Night mode.
    private void nightModeListenerMethod() {
        Switch swNight = findViewById(R.id.swNight);
        swNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] textviews = {R.id.swNight, R.id.tvTitle, R.id.rbRegular, R.id.rbBasic, R.id.rbPremium, R.id.tvLblPackage, R.id.tvLblPipe,R.id.tvPipe, R.id.tvLblPrev, R.id.tvLblNew, R.id.tvLblHistory, R.id.tvLblBill, R.id.etResult, R.id.etPrev, R.id.etNew};
                ConstraintLayout clMain = findViewById(R.id.clMain);
                if (swNight.isChecked()) {
                    editor.putBoolean("night", true);
                    clMain.setBackgroundColor(Color.BLACK);
                    for (int res : textviews) {
                        TextView view1 = findViewById(res);
                        view1.setTextColor(Color.WHITE);
                    }
                } else {
                    editor.putBoolean("night", false);
                    clMain.setBackgroundColor(Color.WHITE);
                    for (int res : textviews) {
                        TextView view1 = findViewById(res);
                        view1.setTextColor(Color.BLACK);
                    }
                }
                editor.apply();
            }
        });
    }

    // Milestone B: Show History.
    private void setHistoryAdapter() {
        ListView lvHistory = findViewById(R.id.lvHistory);
        billsAdapter = new BillsAdapter(getBaseContext(), R.layout.bills_layout, bills);
        lvHistory.setAdapter(billsAdapter);
    }

    // Milestone 3: Calculate bill.
    private void btnCalculateListenerMethod() {
        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etPrev = findViewById(R.id.etPrev);
                int prev = Integer.parseInt(etPrev.getText().toString());
                EditText etNext = findViewById(R.id.etNew);
                int curr = 0;
                try {
                    curr = Integer.parseInt(etNext.getText().toString());
                } catch (Exception e) {
                    BillDialogFragment dialog = new BillDialogFragment();
                    dialog.show(getSupportFragmentManager(), "vince");
                    return;
                }
                if (prev > curr) {
                    BillDialogFragment dialog = new BillDialogFragment();
                    dialog.show(getSupportFragmentManager(), "vince");
                    return;
                }
                // Note: Instead of getting the pipe instance, I have created another Pipe instance given
                // the pipe_brand and pipe_diameter. You can use these in your Content Values too.
                TextView tvPipe = findViewById(R.id.tvPipe);
                String pipe = tvPipe.getText().toString();
                String pipe_brand = pipe.split("/")[0];
                double pipe_diameter = Double.parseDouble(pipe.split("/")[1]);
                Pipe type = new Pipe(pipe_brand, pipe_diameter);
                RadioButton rbBasic = findViewById(R.id.rbBasic);
                RadioButton rbRegular = findViewById(R.id.rbRegular);
                RadioButton rbPremium = findViewById(R.id.rbPremium);
                int pack = 0;
                if (rbBasic.isChecked()) {
                    pack = 1; // Basic Package
                } else if (rbRegular.isChecked()) {
                    pack = 2; // Regular Package
                } else {
                    pack = 3; // Premium Package
                }
                Bill new_bill = new Bill(prev, curr, type, pack, month);
                bills.add(new_bill);
                last_consumption = curr - prev;
                billsAdapter.notifyDataSetChanged();
                month++;
                double bill = new_bill.get_bill();
                EditText etResult = findViewById(R.id.etResult);
                etResult.setText(bill + "");
                etPrev.setText(curr + "");
                etNext.setText("");

                // TODO Milestone 2-3: use Content Resolver here and use Content Values
                //  to insert all data in columns into the database as defined in Bill.java class
            }
        });
    }

    @Override
    public void onYesListenerMethod(DialogFragment dialog) {
        EditText etPrev = findViewById(R.id.etPrev);
        int prev = Integer.parseInt(etPrev.getText().toString());
        int read = prev + last_consumption;
        EditText etNext = findViewById(R.id.etNew);
        etNext.setText(read + "");
    }

    @Override
    public void onNoListenerMethod(DialogFragment dialog) {
        EditText etNext = findViewById(R.id.etNew);
        etNext.setText("");
    }

    // TODO Milestone 3-2: implement Cursor Loader inherited methods
    //  Tip: the concept is the same with the Notes activity:
    //   onCreateLoader will initialize the CursorLoader and the
    //   onLoadFinished will collect all data and store them into the bills ArrayList
    //  Tip: for the Bill's pipe type, use the constructor: new Pipe(brand, diameter)
}