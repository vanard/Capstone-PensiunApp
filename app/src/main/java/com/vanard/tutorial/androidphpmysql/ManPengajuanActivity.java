package com.vanard.tutorial.androidphpmysql;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class ManPengajuanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PICK_PDF_REQUEST = 1;
    private static final String UPLOAD_PDF = "http://192.168.0.100:8012/test/upload.php";
    EditText Ket, nFile, unKerja, name;
    Button bnAdd, bnDate, bnUp, bnLoad;
    TextView shDate;
    int year, month, day;
    Calendar c = Calendar.getInstance();
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_pengajuan);

        requestStoragePermission();

        nFile = (EditText) findViewById(R.id.editText);
        name = (EditText) findViewById(R.id.name);
        unKerja = (EditText) findViewById(R.id.unKerja);
        Ket = (EditText)findViewById(R.id.reason);
        shDate = (TextView)findViewById(R.id.shDate);
        bnAdd = (Button)findViewById(R.id.bnAdd);
        bnDate = (Button)findViewById(R.id.picDate);
        bnUp = (Button) findViewById(R.id.up);
        bnLoad = (Button) findViewById(R.id.upload);

        bnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ManPengajuanActivity.this, ManPengajuanActivity.this, year, month, day);
                datePickerDialog.show();

            }
        });

        bnUp.setOnClickListener(this);
        bnLoad.setOnClickListener(this);
        bnAdd.setOnClickListener(this);
    }

    public void uploadMultipart() {
        String name = nFile.getText().toString().trim();
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, uploadId, UPLOAD_PDF)
                        .addFileToUpload(path, "pdf")
                        .addParameter("name", name)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(3)
                        .startUpload();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            bnUp.setText("Selected");
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        shDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
    }

    private void addData(){

        final String nama = name.getText().toString().trim();
        final String un_kerja = unKerja.getText().toString().trim();
        final String ket = Ket.getText().toString().trim();
        final String date = shDate.getText().toString().trim();

        class AddData extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ManPengajuanActivity.this, "Adding . . .", "Please Wait . . .", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ManPengajuanActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_NAME, nama);
                params.put(Config.KEY_UNIT_KERJA, un_kerja);
                params.put(Config.KEY_ALASAN, ket);
                params.put(Config.KEY_TGL_PENSIUN, date);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD,params);
                return res;
            }
        }

        AddData ae = new AddData();
        ae.execute();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v==bnAdd)
            addData();
        if (v==bnUp)
            showFileChooser();
        if (v==bnLoad)
            uploadMultipart();
    }
}
