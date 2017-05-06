package com.vanard.tutorial.androidphpmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tLogin;
    private EditText name, password, email;
    private Button bnRegist;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        tLogin = (TextView)findViewById(R.id.login);

        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        bnRegist = (Button)findViewById(R.id.bn_Regist);

        progressDialog = new ProgressDialog(this);
        bnRegist.setOnClickListener(this);
        tLogin.setOnClickListener(this);
    }

    private void registerUser(){
        final String mail = email.getText().toString().trim();
        final String nama = name.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        progressDialog.setMessage("Register User . . .");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",nama);
                params.put("email",mail);
                params.put("password",pass);
                return params;
            }
        };

        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view){
        if (view==bnRegist)
            registerUser();
        if (view==tLogin)
            startActivity(new Intent(this, LoginActivity.class));
    }

}
