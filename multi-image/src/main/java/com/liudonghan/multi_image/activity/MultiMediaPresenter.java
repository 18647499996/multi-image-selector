package com.liudonghan.multi_image.activity;


import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liudonghan.multi_image.ADMultiImageSelector;
import com.liudonghan.multi_image.R;
import com.liudonghan.multi_image.adapter.FolderFileAdapter;
import com.liudonghan.mvp.ADBaseExceptionManager;
import com.liudonghan.mvp.ADBaseRequestResult;
import com.liudonghan.mvp.ADBaseSubscription;
import com.liudonghan.utils.ADArrayUtils;
import com.liudonghan.utils.ADCursorManageUtils;
import com.liudonghan.utils.ADScreenUtils;
import com.liudonghan.view.snackbar.ADSnackBarManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MultiMediaPresenter extends ADBaseSubscription<MultiMediaContract.View> implements MultiMediaContract.Presenter {

    public List<ADCursorManageUtils.ImageFolderModel.MediaModel> selectorMediaModel = new ArrayList<>();

    MultiMediaPresenter(MultiMediaContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void getMediaFile(final List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData, final int mediaType) {
        Observable.unsafeCreate(new Observable.OnSubscribe<List<ADCursorManageUtils.ImageFolderModel>>() {
            @Override
            public void call(Subscriber<? super List<ADCursorManageUtils.ImageFolderModel>> subscriber) {
                switch (mediaType) {
                    case 1:
                        subscriber.onNext(ADCursorManageUtils.getInstance(getContext()).getImageFolder());
                        break;
                    case 2:
                        subscriber.onNext(ADCursorManageUtils.getInstance(getContext()).getVideoFolder());
                        break;
                    case 3:
                        subscriber.onNext(ADCursorManageUtils.getInstance(getContext()).getImageOrVideoFile());
                        break;
                    default:
                        break;
                }
            }
        }).doOnNext(new Action1<List<ADCursorManageUtils.ImageFolderModel>>() {
            @Override
            public void call(List<ADCursorManageUtils.ImageFolderModel> imageFolderModels) {
                if (mediaType == 1 || mediaType == 3) {
                    List<ADCursorManageUtils.ImageFolderModel.MediaModel> imageFile = ADCursorManageUtils.getInstance(getContext()).getImageFile();
                    if (!ADArrayUtils.isEmpty(imageFile)) {
                        ADCursorManageUtils.ImageFolderModel videoFolderModel = new ADCursorManageUtils.ImageFolderModel();
                        videoFolderModel.setMediaPath(imageFile);
                        videoFolderModel.setDirName("所有图片");
                        videoFolderModel.setDirPath("");
                        videoFolderModel.setSelect(true);
                        videoFolderModel.setFileCount(imageFile.size());
                        videoFolderModel.setCoverPath(imageFile.get(0).getFilePath());
                        imageFolderModels.add(0, videoFolderModel);
                    }
                }
                if (mediaType == 2 || mediaType == 3) {
                    List<ADCursorManageUtils.ImageFolderModel.MediaModel> videoFile = ADCursorManageUtils.getInstance(getContext()).getVideoFile();
                    if (!ADArrayUtils.isEmpty(videoFile)) {
                        ADCursorManageUtils.ImageFolderModel imageFolderModel = new ADCursorManageUtils.ImageFolderModel();
                        imageFolderModel.setMediaPath(videoFile);
                        imageFolderModel.setDirName("所有视频");
                        imageFolderModel.setDirPath("");
                        imageFolderModel.setFileCount(videoFile.size());
                        imageFolderModel.setCoverPath(videoFile.get(0).getFilePath());
                        imageFolderModels.add(0, imageFolderModel);
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ADBaseRequestResult<List<ADCursorManageUtils.ImageFolderModel>>() {
                    @Override
                    protected void onCompletedListener() {

                    }

                    @Override
                    protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

                    }

                    @Override
                    protected void onNextListener(List<ADCursorManageUtils.ImageFolderModel> imageFolderModels) {
                        view.showMediaList(imageFolderModels);
                    }
                });
    }

    @Override
    public PopupWindow getListPopupWindow(MultiMediaActivity multiMediaActivity, final FolderFileAdapter folderFileAdapter, RelativeLayout activityMultiMediaRel) {
        Point point = ADScreenUtils.getInstance().getScreenSize(multiMediaActivity);
        View view = View.inflate(getContext(), R.layout.pop_ad_folder_file, null);
        RecyclerView recyclerView = view.findViewById(R.id.pop_folder_file_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(folderFileAdapter);
        PopupWindow popupWindow = new PopupWindow(multiMediaActivity);
        popupWindow.setContentView(view);
        popupWindow.setAnimationStyle(R.style.AD_PopupWindow_Style_Bottom);
        popupWindow.setWidth(point.x);
        popupWindow.setHeight((int) (point.y * (4.5f / 8.0f)));
        return popupWindow;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View views, int position, PopupWindow listPopupWindow, int maxCount, int mode, MultiMediaActivity multiMediaActivity) {
        if (adapter instanceof FolderFileAdapter) {
            listPopupWindow.dismiss();
            ADCursorManageUtils.ImageFolderModel imageFolderModel = (ADCursorManageUtils.ImageFolderModel) adapter.getItem(position);
            view.switchFolderFile(imageFolderModel);
        } else {
            ADCursorManageUtils.ImageFolderModel.MediaModel mediaModel = (ADCursorManageUtils.ImageFolderModel.MediaModel) adapter.getItem(position);
            if (-1 == Objects.requireNonNull(mediaModel).getId()) {
                // 拍照
                if (selectorMediaModel.size() == maxCount) {
                    ADSnackBarManager.getInstance().showWarn(getContext(), "超出最大限制");
                    return;
                }
                view.showCamera();
            } else {
                if (mode == 2) {
                    // 单选
                    selectorMediaModel.add(mediaModel);
                    singleSelector(multiMediaActivity);
                    return;
                }
                // 多选
                if (selectorMediaModel.contains(mediaModel)) {
                    selectorMediaModel.remove(mediaModel);
                } else {

                    if (selectorMediaModel.size() == maxCount) {
                        ADSnackBarManager.getInstance().showWarn(getContext(), "超出最大限制");
                        return;
                    }
                    selectorMediaModel.add(mediaModel);
                }
                view.showSelectorMediaFile(mediaModel, position, selectorMediaModel);
            }
        }
    }

    @Override
    public void singleSelector(MultiMediaActivity multiMediaActivity) {
        if (null == ADMultiImageSelector.getInstance().getOnMultiImageSelectorListener()) {
            Log.i("Mac_Liu", "Please set the callback function");
            multiMediaActivity.finish();
            return;
        }
        if (ADArrayUtils.isEmpty(selectorMediaModel)) {
            ADSnackBarManager.getInstance().showWarn(getContext(), "请选择多媒体文件或图片");
            return;
        }
        ADMultiImageSelector.getInstance().getOnMultiImageSelectorListener().onMediaResult(selectorMediaModel);
        multiMediaActivity.finish();
    }
}