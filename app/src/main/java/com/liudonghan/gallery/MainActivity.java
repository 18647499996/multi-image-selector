package com.liudonghan.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.liudonghan.multi_image.MultiImageSelector;
import com.liudonghan.multi_image.permission.LiuPermission;
import com.liudonghan.multi_image.permission.OnPermission;
import com.liudonghan.multi_image.permission.Permission;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.activity_main_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiuPermission
                        .with(MainActivity.this)
                        .permission(Permission.WRITE_EXTERNAL_STORAGE)
                        .permission(Permission.READ_EXTERNAL_STORAGE)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll){
                                    MultiImageSelector
                                            .create()
                                            .showCamera(true)
                                            .multi()
                                            .count(9)
                                            .start(MainActivity.this, 1);
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {

                            }
                        });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            List<String> mListStr = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            Log.d("接收数据：", mListStr.toString());
        }
    }
}