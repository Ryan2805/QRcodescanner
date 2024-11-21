package com.example.qrscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonScan;
    private EditText etName, etAddress;


    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonScan = findViewById(R.id.buttonScan);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);


        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(this);
        etAddress.setOnClickListener(view -> {

            String url = etAddress.getText().toString();
            if (!url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        qrScan.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject obj = new JSONObject(result.getContents());
                    etName.setText(obj.getString("title"));
                    etAddress.setText(obj.getString("website"));
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
