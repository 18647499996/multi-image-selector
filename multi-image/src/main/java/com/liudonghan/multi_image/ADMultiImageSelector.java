package com.liudonghan.multi_image;

import android.app.Activity;

import com.liudonghan.multi_image.activity.MultiMediaActivity;
import com.liudonghan.utils.ADCursorManageUtils;
import com.liudonghan.utils.ADIntentManager;
import com.liudonghan.utils.ADPermissionManager;
import com.liudonghan.view.snackbar.ADSnackBarManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择器
 * Created by nereo on 16/3/17.
 */
public class ADMultiImageSelector {

    public static final String TITLE = "title";
    public static final String MAX_COUNT = "max_count";
    public static final String ORIGIN_DATA = "origin_data";
    public static final String MEDIA_TYPE = "media_type";
    public static final String SHOW_CAMERA = "show_camera";
    public static final String MODE = "mode";

    private Activity activity;
    private boolean showCamera = true;
    private int maxCount = 9;
    private List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData = new ArrayList<>();
    private MediaType mediaType = MediaType.Image;
    private Mode mode = Mode.Multiple;
    private String title;
    private ADMultiImageSelector.OnMultiImageSelectorListener onMultiImageSelectorListener;


    private static volatile ADMultiImageSelector instance = null;

    private ADMultiImageSelector() {
        resetData();
    }

    public void resetData() {
        this.maxCount = 9;
        this.originData = new ArrayList<>();
        this.mediaType = MediaType.Image;
        this.mode = Mode.Multiple;
        this.title = "";
        this.onMultiImageSelectorListener = null;
    }

    public static ADMultiImageSelector getInstance() {
        //single checkout
        if (null == instance) {
            synchronized (ADMultiImageSelector.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADMultiImageSelector();
                }
            }
        }
        return instance;
    }

    public OnMultiImageSelectorListener getOnMultiImageSelectorListener() {
        return onMultiImageSelectorListener;
    }

    public ADMultiImageSelector from(Activity activity) {
        this.activity = activity;
        return this;
    }

    public ADMultiImageSelector title(String title) {
        this.title = title;
        return this;
    }

    public ADMultiImageSelector showCamera(boolean show) {
        showCamera = show;
        return this;
    }

    public ADMultiImageSelector maxCount(int count) {
        maxCount = count;
        return this;
    }

    public ADMultiImageSelector mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public ADMultiImageSelector origin(List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData) {
        this.originData = originData;
        return this;
    }

    public ADMultiImageSelector mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public ADMultiImageSelector request(ADMultiImageSelector.OnMultiImageSelectorListener onMultiImageSelectorListener) {
        this.onMultiImageSelectorListener = onMultiImageSelectorListener;
        return this;
    }

    public void start() {
        ADPermissionManager
                .with(activity)
                .permission(ADPermissionManager.READ_EXTERNAL_STORAGE)
                .permission(ADPermissionManager.WRITE_EXTERNAL_STORAGE)
                .permission(ADPermissionManager.CAMERA)
                .request(new ADPermissionManager.OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            ADIntentManager.getInstance()
                                    .from(activity)
                                    .startClass(MultiMediaActivity.class)
                                    .putExt(TITLE, title)
                                    .putExt(MAX_COUNT, maxCount)
                                    .putExt(ORIGIN_DATA, (Serializable) originData)
                                    .putExt(SHOW_CAMERA, showCamera)
                                    .putExt(MODE, Mode.Multiple == mode ? 1 : 2)
                                    .putExt(MEDIA_TYPE, MediaType.Image == mediaType ? 1 : MediaType.Video == mediaType ? 2 : 3)
                                    .builder();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        ADSnackBarManager.getInstance().showWarn(activity,"请在系统设置中打开权限");
                    }
                });
    }

    public interface OnMultiImageSelectorListener {
        /**
         * 媒体列表回调
         *
         * @param results 列表数据
         */
        void onMediaResult(List<ADCursorManageUtils.ImageFolderModel.MediaModel> results);
    }

    public enum MediaType {
        Image,
        Video,
        ImageOrVideo
    }

    public enum Mode {
        Single,
        Multiple
    }
}
