package com.vanard.tutorial.androidphpmysql;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Vanard on 5/2/2017.
 */

public class MySQLClient {
    private static final String DATA_INSERT_URL="http://192.168.0.100:8012/test/spinner/crud.php";
    private final Context c;
    public MySQLClient(Context c) {
        this.c = c;
    }

    public void add(Data d, final View...inputViews)
    {
        if(d==null)
        {
            Toast.makeText(c, "No Data To Updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","update")
                    .addBodyParameter("status",d.getStatus())
                    .addBodyParameter("keterangan",d.getKeterangan())
                    .setTag("TAG_ADD")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(response != null)
                                try {
                                    String responseString = response.get(0).toString();
                                    Toast.makeText(c, "SERVER RESPONSE : " + responseString, Toast.LENGTH_SHORT).show();
                                    if (responseString.equalsIgnoreCase("Success")) {
                                        Spinner mySpin = (Spinner)inputViews[0];
                                        EditText kete = (EditText)inputViews[1];
                                    }else {
                                        Toast.makeText(c, "WASN'T SUCCESSFUL. ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(c, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(c, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
