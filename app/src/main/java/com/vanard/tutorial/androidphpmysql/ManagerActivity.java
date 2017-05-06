package com.vanard.tutorial.androidphpmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tName;
    Button bnList, bnInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        tName = (TextView)findViewById(R.id.tName);
        bnList = (Button)findViewById(R.id.bn_View);
        bnInput = (Button)findViewById(R.id.input);

        tName.setText(SharedPrefManager.getInstance(this).getUserName());

        bnList.setOnClickListener(this);
        bnInput.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).loggout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v==bnList)
            startActivity(new Intent(this, ListPengajuan.class));
        if (v==bnInput)
            startActivity(new Intent(this, ManPengajuanActivity.class));
    }
}
