package com.liudonghan.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.liudonghan.media.utils.MediaSelector;
import com.liudonghan.multi_image.ADMultiImageSelector;
import com.liudonghan.utils.ADCursorManageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.activity_main_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADMultiImageSelector.getInstance()
                        .from(MainActivity.this)
                        .title("贝米")
                        .mediaType(ADMultiImageSelector.MediaType.Image)
                        .mode(ADMultiImageSelector.Mode.Multiple)
                        .showCamera(true)
                        .maxCount(9)
                        .origin(originData)
                        .request(results -> {
                            originData.clear();
                            originData.addAll(results);
                            for (int i = 0; i < originData.size(); i++) {
                                Log.i("Mac_Liu", originData.get(i).getFilePath());
                            }
                        })
                        .start();

            }
        });
        findViewById(R.id.activity_main_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaSelector
                        .get(MainActivity.this)
                        .setMediaType(MediaSelector.PICTURE)
                        .setMaxCount(9)
                        .setSelectMode(MediaSelector.MODE_MULTI)
                        .setDefaultList(new ArrayList<>(Collections.singleton("s")))
                        .showCamera(true)
                        .setListener(new MediaSelector.MediaSelectorListener() {
                            @Override
                            public void onMediaResult(List<String> resultList) {
                                Log.i("图片列表：", resultList.toString());
                            }
                        })
                        .jump();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (1 == requestCode) {
//            List<String> mListStr = data.getStringArrayListExtra(ADMultiImageSelector.EXTRA_RESULT);
//            Log.d("接收数据：", mListStr.toString());
//        }
    }
}