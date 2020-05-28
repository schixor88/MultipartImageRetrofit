package com.kushagra.imageuploader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kushagra.imageuploader.core.ApiCore;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "Uploader";

    private ApiCore.GenericDefinitionInterface definitionInterface;
    private Button upload;
    private WebView webView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upload = findViewById(R.id.button);
        webView = findViewById(R.id.result);
        dialog = new ProgressDialog(MainActivity.this);
        upload.setOnClickListener(handleButtonClick);

        definitionInterface = ApiCore.getRetrofit().create(ApiCore.GenericDefinitionInterface.class);
    }

    private View.OnClickListener handleButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImagePicker.Companion.with(MainActivity.this).galleryOnly().start();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            File file = ImagePicker.Companion.getFile(data);

            MultipartBody.Part requestImage = null;

            RequestBody  requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            requestImage = MultipartBody.Part.createFormData("image",file.getName(),requestFile);

            callForApiResponse(requestImage);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void callForApiResponse(MultipartBody.Part requestImage){


        dialog.setTitle("Loading");
        dialog.setMessage("Timeout set to 1 minute...");
        dialog.show();

        Call<JsonArray> call = definitionInterface.uploadFile(requestImage);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d(TAG,response.toString());
                dialog.dismiss();
                webView.loadDataWithBaseURL(null, response.body().toString(), "text/html", "utf-8", null);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                dialog.dismiss();
                webView.loadDataWithBaseURL(null,t.getMessage(),"text/html", "utf-8", null);
            }
        });

    }
}
