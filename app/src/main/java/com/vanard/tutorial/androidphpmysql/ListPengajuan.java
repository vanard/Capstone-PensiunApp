package com.vanard.tutorial.androidphpmysql;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPengajuan extends AppCompatActivity {
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<Data> mItems;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengajuan);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewTemp);

        pd = new ProgressDialog(ListPengajuan.this);
        mItems = new ArrayList<>();

        loadJson();

        mManager = new LinearLayoutManager(ListPengajuan.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterData(ListPengajuan.this, mItems);
        mRecyclerview.setAdapter(mAdapter);

    }

    private void loadJson() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, Config.URL_GET_ALL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Data md = new Data();
                                md.setId(data.getString("id"));
                                md.setNama(data.getString("name"));
                                md.setUnitkerja(data.getString("unit_kerja"));
                                md.setAlasan(data.getString("alasan"));
                                md.setTgl_pensiun(data.getString("tgl_pensiun"));
                                md.setTgl_pengajuan(data.getString("tgl_pengajuan"));
                                md.setStatus(data.getString("status"));
                                md.setKeterangan(data.getString("keterangan"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance(getApplicationContext()).addToReqQueue(reqData);
    }
}
