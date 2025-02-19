package com.learning.fullcrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // perintah untuk mendefinisikan view
    private EditText etName;
    private EditText etPosition;
    private EditText etSalary;
    private Button btnSave;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisialisasi dari view
        etName      = (EditText) findViewById(R.id.etName);
        etPosition  = (EditText) findViewById(R.id.etPosition);
        etSalary    = (EditText) findViewById(R.id.etSalary);
        btnSave     = (Button) findViewById(R.id.btnSave);
        toolbar     = (Toolbar) findViewById(R.id.tlCreate);

        // toolbar
        toolbar.setTitle("Create New");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // setting listener to button
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            saveEmployee();
        }
        else {
            Toast.makeText(this, "Error,..!", Toast.LENGTH_LONG).show();
        }
    }

    // perintah untuk menambahkan data pegawai baru
    private void saveEmployee() {
        final String name           = etName.getText().toString().trim();
        final String position       = etPosition.getText().toString().trim();
        final String salary         = etSalary.getText().toString().trim();

        class SaveEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Processing...", "Wait", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject content = new JSONObject(s);
                    String msg = content.getString("msg");
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(MainActivity.this, MainListing.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_NAMA, name);
                params.put(konfigurasi.KEY_POSISI, position);
                params.put(konfigurasi.KEY_GAJI, salary);

                RequestHandler rh   = new RequestHandler();
                String result   = rh.sendPostRequest(konfigurasi.URL_ADD, params);

                // clear edit text on form
                etName.getText().clear();
                etPosition.getText().clear();
                etSalary.getText().clear();

                return result;
            }
        }

        SaveEmployee se     = new SaveEmployee();
        se.execute();
    }

}
