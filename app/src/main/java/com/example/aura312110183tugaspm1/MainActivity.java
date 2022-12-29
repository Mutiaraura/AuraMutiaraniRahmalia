package com.example.aura312110183tugaspm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText phoneNo;
    FloatingActionButton callbtn;
    static int PERMISSION_CODE = 100;
    private WebView webView;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.com");

        phoneNo = findViewById(R.id.editTextPhone);
        callbtn = findViewById(R.id.callbtn);

        textView = findViewById(R.id.results);
        button = findViewById(R.id.buttonFetch);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();

            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneno = phoneNo.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneno));
                startActivity(i);
            }
        });
    }
    public void fetchData () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://demo.codeseasy.com/files/2022/parsing-json-data-in-android.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parseJson, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Dont work");
            }
        });

        queue.add(stringRequest);
    }
    public void parseJson (String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("students");
            for (int i = 0; i < jsonArray.length(); i++) {
                String nameVar, percentageVar, gradeVar;
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                nameVar = jsonObject2.getString("name");
                percentageVar = jsonObject2.getString("percentage");
                gradeVar = jsonObject2.getString("grade");
                textView.append("Name: " + nameVar + "Percentage: " + percentageVar + " Grade: " + gradeVar + "\n");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}