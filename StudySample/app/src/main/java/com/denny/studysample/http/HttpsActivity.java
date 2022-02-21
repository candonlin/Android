package com.denny.studysample.http;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denny.studysample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpsActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = HttpsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https);
        init();
    }

    private void init() {
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                OkHttpClient client = null;
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(SSLTool.getSSLSocketFactory())
                        .hostnameVerifier(SSLTool.getHostnameVerifier())
                        .build();

                JSONObject object = new JSONObject();
                try {
                    object.put("EMPNO", "sa");
                    object.put("EMPPASSWORD", "1234");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(object.toString(), JSON);
                Request request = new Request.Builder().url("https://192.168.200.91/regalstudy/api/login")
                        .post(body).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.e(TAG, response.body().string());

                    }
                });
                break;
        }
    }
}