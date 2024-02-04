package com.liudonghan.multi_image.activity;


import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liudonghan.multi_image.adapter.FolderFileAdapter;
import com.liudonghan.mvp.ADBasePresenter;
import com.liudonghan.mvp.ADBaseView;
import com.liudonghan.utils.ADCursorManageUtils;

import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public interface MultiMediaContract {

    interface View extends ADBaseView<Presenter> {

        /**
         * 显示媒体文件列表
         *
         * @param imageFolderModels 媒体文件列表
         */
        void showMediaList(List<ADCursorManageUtils.ImageFolderModel> imageFolderModels);

        /**
         * 切换文件夹文件
         *
         * @param imageFolderModel 文件夹信息
         */
        void switchFolderFile(ADCursorManageUtils.ImageFolderModel imageFolderModel);

        /**
         * 选择媒体文件
         *
         * @param mediaModel         文件数据
         * @param position           索引
         * @param selectorMediaModel 选中列表
         */
        void showSelectorMediaFile(ADCursorManageUtils.ImageFolderModel.MediaModel mediaModel, int position, List<ADCursorManageUtils.ImageFolderModel.MediaModel> selectorMediaModel);

        /**
         * 显示图片
         */
        void showCamera();

        /**
         * 单选
         *
         * @param selectorMediaModel 选中列表
         */
        void showSingleSelector(List<ADCursorManageUtils.ImageFolderModel.MediaModel> selectorMediaModel);
    }

    interface Presenter extends ADBasePresenter {

        /**
         * 获取媒体文件
         *
         * @param originData 原数据
         * @param mediaType  媒体文件类型
         */
        void getMediaFile(List<ADCursorManageUtils.ImageFolderModel.MediaModel> originData, int mediaType);

        /**
         * 获取弹窗列表
         *
         * @param multiMediaActivity    activity引用
         * @param folderFileAdapter     适配器引用
         * @param activityMultiMediaRel 弹出组件
         * @return PopupWindow
         */
        PopupWindow getListPopupWindow(MultiMediaActivity multiMediaActivity, FolderFileAdapter folderFileAdapter, RelativeLayout activityMultiMediaRel);

        /**
         * 条目点击事件
         *  @param adapter         适配器引用
         * @param views           view引用
         * @param position        索引
         * @param listPopupWindow PopupWindow弹窗
         * @param maxCount        最大选中数量
         * @param mode
         * @param multiMediaActivity
         */
        void onItemClick(BaseQuickAdapter adapter, android.view.View views, int position, PopupWindow listPopupWindow, int maxCount, int mode, MultiMediaActivity multiMediaActivity);

        /**
         * 单选
         *
         * @param multiMediaActivity activity引用
         */
        void singleSelector(MultiMediaActivity multiMediaActivity);
    }
}