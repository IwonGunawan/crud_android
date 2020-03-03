package com.learning.fullcrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainListing extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener {

    private ListView listItem;
    private FloatingActionButton btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listing);

        getJSON();
        listItem    = (ListView) findViewById(R.id.listItem);
        btnCreate   = (FloatingActionButton) findViewById(R.id.btnCreate);

        listItem.setOnItemClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MainDetail.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId    = map.get(konfigurasi.KEY_ID).toString();
        intent.putExtra("employee_id", empId);
        startActivity(intent);
    }

    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainListing.this, "Fetching Data", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_ALL);
                return s;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void showEmployee(String result) {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(result);
            Integer status    = jsonObject.getInt("status");
            JSONArray data    = jsonObject.getJSONArray("data");

            if (status == 200) {
                for (int i=0; i < data.length(); i++) {
                    JSONObject jsonObject1 = data.getJSONObject(i);
                    String empId        = jsonObject1.getString(konfigurasi.KEY_ID);
                    String empName      = jsonObject1.getString(konfigurasi.KEY_NAMA);
                    String empPosition  = jsonObject1.getString(konfigurasi.KEY_POSISI);

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("id", empId);
                    employees.put("nama", empName);
                    employees.put("posisi", " > " + empPosition);
                    list.add(employees);
                }
            }
            else {
                Toast.makeText(MainListing.this, "Failed Fetch Data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                MainListing.this, list, R.layout.list_item,
                new String[] {"id", "nama", "posisi"},
                new int[] {R.id.emp_id, R.id.emp_name, R.id.emp_position}
        );
        listItem.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if (v == btnCreate) {
            Toast.makeText(this, "hello create", Toast.LENGTH_LONG).show();
        }
    }
}
