package com.learning.fullcrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView etId;
    private EditText etName;
    private EditText etPosition;
    private EditText etSalary;
    private Button btnUpdate;
    private Button btnDelete;

    private String employee_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        // get intent with param : employee_id
        Intent intent = getIntent();
        employee_id = intent.getStringExtra("employee_id");

        // initialization
        etId            = (TextView) findViewById(R.id.etId);
        etName          = (EditText) findViewById(R.id.etName);
        etPosition      = (EditText) findViewById(R.id.etPosition);
        etSalary        = (EditText) findViewById(R.id.etSalary);
        btnUpdate       = (Button) findViewById(R.id.btnUpdate);
        btnDelete       = (Button) findViewById(R.id.btnDelete);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        // get detail employee
        etId.setText(employee_id);
        getEmployee(employee_id);
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpdate) {
            update();
        }
        else if (v == btnDelete) {
             confDelete();
        }
    }

    public void getEmployee(final String employee_id) {

        class GetEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDetail.this, "Fetching Data", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh   = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DETAIL, employee_id);
                return s;
            }
        }

        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    public void showEmployee(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json);
            Integer status  = result.getInt("status");
            String msg      = result.getString("msg");
            JSONArray dataa  = result.getJSONArray("data");
            if (status == 200) {
                JSONObject data = dataa.getJSONObject(0);
                String name         = data.getString("nama");
                String position     = data.getString("posisi");
                String salary       = data.getString("gaji");

                etName.setText(name);
                etPosition.setText(position);
                etSalary.setText(salary);
            }
            else {
                Toast.makeText(MainDetail.this, msg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        final String id         = etId.getText().toString().trim();
        final String name       = etName.getText().toString().trim();
        final String position   = etPosition.getText().toString().trim();
        final String salary     = etSalary.getText().toString().trim();

        class Update extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDetail.this, "updating", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainDetail.this, "success update data", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("name", name);
                map.put("position", position);
                map.put("salary", salary);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE, map);

                return s;
            }
        }
        Update update = new Update();
        update.execute();
    }

    protected void confDelete() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure delete this data ?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                        startActivity(new Intent(MainDetail.this, MainListing.class));
                    }
                }
        );
        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void delete() {

        class Delete extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainDetail.this, "Delete", "Processing Delete...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                try {
                    JSONObject content = new JSONObject(s);
                    String msg  = content.getString("msg");
                    Toast.makeText(MainDetail.this, msg, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE, employee_id);
                return s;
            }
        }

        Delete delete = new Delete();
        delete.execute();
    }


}
