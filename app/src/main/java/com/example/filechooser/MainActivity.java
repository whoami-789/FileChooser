package com.example.filechooser;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        byte[] byt = getBytesFromUri(getApplicationContext(), uri);
                        textView.setText(new String(byt));

                    }
                }
            }
    );

    public void openFile(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        String[] types = {"text/xml", "application/csv", "application/json", "text/txt"};
        data.putExtra(Intent.EXTRA_MIME_TYPES, types);
        data = Intent.createChooser(data, "Выберите файл");
        sActivityResultLauncher.launch(data);
    }

    byte[] getBytesFromUri (Context context, Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
            int buffSize = 1024;
            byte[] buff = new byte[buffSize];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1){
                byteBuff.write(buff, 0, len);
            }
            return byteBuff.toByteArray();
        }
        catch (Exception ex){

        }return null;
    }
}