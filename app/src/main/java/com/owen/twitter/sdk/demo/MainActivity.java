package com.owen.twitter.sdk.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.owen.twitter.sdk.Callback;
import com.owen.twitter.sdk.TwitterSDK;
import com.owen.twitter.sdk.auth.TwitterSession;
import com.owen.twitter.sdk.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(TwitterSDK.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        if(requestCode == 2000) {
            if(RESULT_OK == resultCode) {
                Uri uri = data.getData();
                String msg = "Share to twitter test";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "#Tags " + msg);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
//                intent.setType("video/*");
//                intent.setPackage("com.twitter.android");
                intent.setClassName("com.twitter.android", "com.twitter.composer.ComposerActivity");
                startActivity(intent);

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_to_twitter:
                TwitterSDK.login(this, new Callback<TwitterSession>() {
                    @Override
                    public void onResult(int code, String msg, TwitterSession data) {
                        Toast.makeText(MainActivity.this, null == data ? "Login failed" : data.toString(), Toast.LENGTH_LONG).show();
                        LogUtils.e(String.format("Login Result\ncode = %d\nmsg = %s\ndata = %s", code, msg, null == data ? "" : data.toString()));
                    }
                });



                break;
            case R.id.btn_share_to_twitter:
//                String msg = "Share to twitter test";
//                Uri uri = Uri.parse("file:///storage/emulated/0/DCIM/Camera/IMG_20180925_091354.jpg");
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, msg);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent.setType("image/jpeg");
//                intent.setPackage("com.twitter.android");
//                startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                intent.setType("video/*");
                startActivityForResult(intent, 2000);
//                TwitterSDK.share(this, null, null);
                break;
            default:
                break;
        }
    }


    private void shareToTwitter() {
//        Uri uri = data.getData();
//        String msg = "Share to twitter test";
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, msg);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        intent.setType("image/jpeg");
//        intent.setPackage("com.twitter.android");
//        startActivity(intent);
    }
}
