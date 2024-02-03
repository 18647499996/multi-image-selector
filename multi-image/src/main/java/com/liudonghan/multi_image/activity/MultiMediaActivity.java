package com.liudonghan.multi_image.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liudonghan.multi_image.ADMultiImageSelector;
import com.liudonghan.multi_image.R;
import com.liudonghan.multi_image.adapter.FolderFileAdapter;
import com.liudonghan.multi_image.adapter.MultiMediaAdapter;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.utils.ADArrayUtils;
import com.liudonghan.utils.ADCursorManageUtils;
import com.liudonghan.view.radius.ADTextView;
import com.liudonghan.view.recycler.ADRecyclerView;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Description：媒体选择器
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MultiMediaActivity extends ADBaseActivity<MultiMediaPresenter> implements MultiMediaContract.View, BaseQuickAdapter.OnItemClickListener {

    ImageView activityMultiMediaImgBack;
    ADTextView activityMultiMediaTvSucceed;
    ADRecyclerView activityMultiMediaRv;
    Button activityMultiMediaBtnCategory;
    RelativeLayout activityMultiMediaRel;
    TextView activityMultiMediaTvTitle;

    private FolderFileAdapter folderFileAdapter;
    private MultiMediaAdapter multiMediaAdapter;
    private PopupWindow listPopupWindow;
    private String title;
    private int maxCount;
    private List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData = new ArrayList<>();

    @Override
    protected int getLayout() throws RuntimeException {
        return R.layout.activity_ad_multi_media;
    }

    @Override
    protected ADTitleBuilder initBuilderTitle() throws RuntimeException {
        return null;
    }

    @Override
    protected MultiMediaPresenter createPresenter() throws RuntimeException {
        title = getIntent().getStringExtra(ADMultiImageSelector.TITLE);
        maxCount = getIntent().getIntExtra(ADMultiImageSelector.MAX_COUNT, 9);
        originData = (List<ADCursorManageUtils.ImageFolderModel.MediaModel>) getIntent().getSerializableExtra(ADMultiImageSelector.ORIGIN_DATA);
        return (MultiMediaPresenter) new MultiMediaPresenter(this).builder(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        immersionBar.statusBarColor(R.color.color_333333).statusBarDarkFont(false).init();
        activityMultiMediaTvTitle = (TextView) findViewById(R.id.activity_multi_media_tv_title);
        activityMultiMediaImgBack = (ImageView) findViewById(R.id.activity_multi_media_img_back);
        activityMultiMediaTvSucceed = (ADTextView) findViewById(R.id.activity_multi_media_tv_succeed);
        activityMultiMediaRv = (ADRecyclerView) findViewById(R.id.activity_multi_rv);
        activityMultiMediaBtnCategory = (Button) findViewById(R.id.activity_multi_media_btn_category);
        activityMultiMediaRel = (RelativeLayout) findViewById(R.id.activity_multi_media_rel);

        activityMultiMediaTvTitle.setText(title);
        activityMultiMediaTvSucceed.setText("完成(" + originData.size() + "/" + maxCount + ")");

        folderFileAdapter = new FolderFileAdapter(R.layout.item_ad_folder_file);
        multiMediaAdapter = new MultiMediaAdapter(R.layout.item_ad_mutli_media, this);
        activityMultiMediaRv.setAdapter(multiMediaAdapter);
        listPopupWindow = mPresenter.getListPopupWindow(this, folderFileAdapter, activityMultiMediaRel);
        mPresenter.getMediaFile(originData);
    }

    @Override
    protected void addListener() throws RuntimeException {
        folderFileAdapter.setOnItemClickListener(this);
        multiMediaAdapter.setOnItemClickListener(this);
        activityMultiMediaBtnCategory.setOnClickListener(this);
        activityMultiMediaTvSucceed.setOnClickListener(this);
        activityMultiMediaImgBack.setOnClickListener(this);
    }

    @Override
    protected void onClickDoubleListener(View view) throws RuntimeException {
        if (view.getId() == R.id.activity_multi_media_btn_category) {
            listPopupWindow.showAsDropDown(activityMultiMediaRel);
        } else if (view.getId() == R.id.activity_multi_media_tv_succeed) {
            if (null == ADMultiImageSelector.getInstance().getOnMultiImageSelectorListener()) {
                Log.i("Mac_Liu", "Please set the callback function");
                MultiMediaActivity.this.finish();
                return;
            }
            if (ADArrayUtils.isEmpty(mPresenter.selectorMediaModel)) {
                ADSnackBarManager.getInstance().showWarn(this, "请选择多媒体文件或图片");
                return;
            }
            ADMultiImageSelector.getInstance().getOnMultiImageSelectorListener().onMediaResult(mPresenter.selectorMediaModel);
            MultiMediaActivity.this.finish();
        } else if (view.getId() == R.id.activity_multi_media_img_back) {
            MultiMediaActivity.this.finish();
        }
    }

    @Override
    protected void onDestroys() throws RuntimeException {

    }

    @Override
    public void setPresenter(MultiMediaContract.Presenter presenter) {
        mPresenter = (MultiMediaPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {
        ADSnackBarManager.getInstance().showError(this, msg);
    }


    @Override
    public void showMediaList(List<ADCursorManageUtils.ImageFolderModel> imageFolderModels) {
        folderFileAdapter.setNewData(imageFolderModels);
        activityMultiMediaBtnCategory.setText(imageFolderModels.get(0).getDirName());
        multiMediaAdapter.setNewData(imageFolderModels.get(0).getMediaPath());
    }

    @Override
    public void switchFolderFile(ADCursorManageUtils.ImageFolderModel imageFolderModel) {
        activityMultiMediaBtnCategory.setText(imageFolderModel.getDirName());
        folderFileAdapter.setSelector(imageFolderModel);
        multiMediaAdapter.set(imageFolderModel, mPresenter.selectorMediaModel);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showSelectorMediaFile(ADCursorManageUtils.ImageFolderModel.MediaModel mediaModel, int position, List<ADCursorManageUtils.ImageFolderModel.MediaModel> selectorMediaModel) {
        multiMediaAdapter.setSelector(mediaModel, position);
        activityMultiMediaTvSucceed.setText("完成(" + selectorMediaModel.size() + "/" + maxCount + ")");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.onItemClick(adapter, view, position, listPopupWindow,maxCount);
    }
}
