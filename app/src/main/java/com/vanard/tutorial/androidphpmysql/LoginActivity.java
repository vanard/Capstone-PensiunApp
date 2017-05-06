package com.vanard.tutorial.androidphpmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, pass;
    Button bnLogin;
    ProgressDialog progressDialog;
    String unk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unk = SharedPrefManager.getInstance(getApplicationContext()).getUnk();
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            if(unk.equals("CEO") || unk.equals("Administrator")) {
                finish();
                startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
            }else {
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }return;
        }

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        bnLogin = (Button)findViewById(R.id.bn_Login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait . . .");

        bnLogin.setOnClickListener(this);
    }

    private void userLogin(){
        final String mail = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error")) {
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(obj.getInt("id"), obj.getString("email"), obj.getString("name"), obj.getString("unit_kerja"));
                        unk = SharedPrefManager.getInstance(getApplicationContext()).getUnk();
                        if(unk.equals("CEO") || unk.equals("Administrator")) {
                            startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", mail);
                params.put("password", password);
                return params;
            }
        };

        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onClick(View view){
        if(view==bnLogin)
            userLogin();
    }
}
