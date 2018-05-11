package com.example.pc.file;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button saveIn, saveEx,readIn,readEx;
    String fileName = "Cuong dep trai";
    String content = "Cuong luc nao cung dep trai";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveIn = findViewById(R.id.saveIn);
        saveEx = findViewById(R.id.saveEx);
        readIn = findViewById(R.id.readIn);
        readEx = findViewById(R.id.readEx);
        textView = findViewById(R.id.txView);

        saveIn.setOnClickListener(this);
        saveEx.setOnClickListener(this);
        readIn.setOnClickListener(this);
        readEx.setOnClickListener(this);
        checkAndRequestPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveIn:
                saveInStorage();
                break;
            case R.id.saveEx:
                saveExStorage();
                break;
            case R.id.readIn:
                readInStorage();
                break;
            case R.id.readEx:
                readExStorage();
                break;
        }
    }


    public void saveInStorage(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"successfully",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveInStorageCache(){
        File file;
        FileOutputStream fileOutputStream;
        file = new File(getCacheDir(),fileName);
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readInStorage(){
        try {
            FileInputStream inputStream= this.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
            }
            textView.setText(stringBuffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveExStorage(){
        if(isExternalStorageReadable()){
            File  file = new File(Environment.getExternalStorageDirectory(),fileName);
            Log.d("aaa",Environment.getExternalStorageDirectory().getAbsolutePath());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(),"successfully",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void readExStorage(){
        File file = new File(Environment.getExternalStorageDirectory(),fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            if((line= bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            textView.setText(stringBuffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
