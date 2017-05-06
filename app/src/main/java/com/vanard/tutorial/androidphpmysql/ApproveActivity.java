package com.vanard.tutorial.androidphpmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApproveActivity extends AppCompatActivity {

    String idnya, namenya, uniknya, reasnya, tglnya, tgldia, sts;
    ProgressDialog pd;
    private TextView name, unKerja, reas,tgl, tvket;
    private EditText kete;
    private Button sub;
    private Spinner mySpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        Intent intent = getIntent();
        final int update = intent.getIntExtra("update", 0);
        idnya = intent.getStringExtra("id");
        namenya = intent.getStringExtra("name");
        uniknya = intent.getStringExtra("unit_kerja");
        reasnya = intent.getStringExtra("alasan");
        tglnya = intent.getStringExtra("tgl_pensiun");
        tgldia = intent.getStringExtra("tgl_pengajuan");
        sts = intent.getStringExtra("status");

        pd = new ProgressDialog(ApproveActivity.this);

        name = (TextView) findViewById(R.id.name);
        unKerja = (TextView) findViewById(R.id.unKerja);
        reas = (TextView) findViewById(R.id.reason);
        tgl = (TextView) findViewById(R.id.tgl);
        tvket = (TextView) findViewById(R.id.tvKete);
        kete = (EditText) findViewById(R.id.kete);
        sub = (Button) findViewById(R.id.bnSub);
        mySpin = (Spinner) findViewById(R.id.spin);

        spinner();

        if (update == 1) {
            name.setText(namenya);
            unKerja.setText(uniknya);
            reas.setText(reasnya);
            tgl.setText(tglnya);
            if (sts.equals("Approved")){
                mySpin.setSelection(1);
                mySpin.setClickable(false);
            }else if (sts.equals("Rejected")){
                mySpin.setSelection(2);
                mySpin.setClickable(false);
            }
        }

        mySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mySpin.getSelectedItem().equals("Approved") || mySpin.getSelectedItem().equals("Rejected")){
                    tvket.setVisibility(View.VISIBLE);
                    kete.setVisibility(View.VISIBLE);
                }else {
                    tvket.setVisibility(View.GONE);
                    kete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //this.submitEvents();
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update==1)
                    Update_data();
            }
        });
    }

    private void submitEvents(){
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spin = mySpin.getSelectedItem().toString();
                String keterangan = kete.getText().toString().trim();
                if (keterangan.length()<1){
                    Toast.makeText(ApproveActivity.this, "Please Fill The Fields", Toast.LENGTH_LONG).show();
                }else {
                    Data d = new Data();
                    d.setStatus(spin);
                    d.setKeterangan(keterangan);
                    new MySQLClient(ApproveActivity.this).add(d, mySpin, kete);
                }
            }
        });
    }

    private void Update_data()
    {
        pd.setMessage("Update Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, Config.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(ApproveActivity.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(ApproveActivity.this,ManagerActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(ApproveActivity.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",idnya);
                map.put("name",namenya);
                map.put("unit_kerja",uniknya);
                map.put("alasan",reasnya);
                map.put("tgl_pensiun",tglnya);
                map.put("status",mySpin.getSelectedItem().toString());
                map.put("keterangan",kete.getText().toString());

                return map;
            }
        };

        AppController.getInstance(this).addToReqQueue(updateReq);
    }



    private void spinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Waiting Approval");
        adapter.add("Approved");
        adapter.add("Rejected");
        mySpin.setAdapter(adapter);
        mySpin.setSelection(0);
    }
}
