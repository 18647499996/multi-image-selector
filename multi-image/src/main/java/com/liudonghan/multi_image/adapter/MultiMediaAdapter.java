package com.liudonghan.multi_image.adapter;

import android.graphics.Point;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liudonghan.multi_image.R;
import com.liudonghan.multi_image.activity.MultiMediaActivity;
import com.liudonghan.utils.ADCursorManageUtils;
import com.liudonghan.utils.ADFormatUtils;
import com.liudonghan.utils.ADImageUtils;
import com.liudonghan.utils.ADScreenUtils;

import java.io.File;
import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/12/23
 */
public class MultiMediaAdapter extends BaseQuickAdapter<ADCursorManageUtils.ImageFolderModel.MediaModel, BaseViewHolder> {

    private int with;
    private ADImageUtils adImageUtils;
    private ADFormatUtils timeFormatUtils;
    private ADCursorManageUtils adCursorManageUtils;

    public MultiMediaAdapter(int layoutResId, MultiMediaActivity multiMediaActivity) {
        super(layoutResId);
        adImageUtils = ADImageUtils.getInstance();
        timeFormatUtils = ADFormatUtils.getInstance();
        adCursorManageUtils = ADCursorManageUtils.getInstance(multiMediaActivity);
        Point point = ADScreenUtils.getInstance().getScreenSize(multiMediaActivity);
        with = point.x / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, ADCursorManageUtils.ImageFolderModel.MediaModel item) {
        helper.setGone(R.id.item_multi_media_img_cover, item.getId() != -1)
                .setGone(R.id.item_multi_media_view_mask, item.getId() != -1)
                .setGone(R.id.item_multi_media_img_check_mark, item.getId() != -1)
                .setGone(R.id.item_mutli_media_img_play, item.getId() != -1)
                .setGone(R.id.item_mutli_media_img_play, item.getId() != -1)
                .setGone(R.id.item_multi_media_tv_duration, item.getId() != -1)
                .setGone(R.id.item_multi_media_tv_camera, item.getId() != -1)
                .setGone(R.id.item_multi_media_tv_camera, item.getId() == -1);
        if (item.getId() == -1) {
            // 拍照
            ImageView textView = helper.getView(R.id.item_multi_media_tv_camera);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.width = with;
            layoutParams.height = with;
            textView.setLayoutParams(layoutParams);
        } else {
            // 图片
            ImageView imageView = adImageUtils.setImageWidthOrHeight((ImageView) helper.getView(R.id.item_multi_media_img_cover), with, with);
            ImageView imageViewMask = adImageUtils.setImageWidthOrHeight((ImageView) helper.getView(R.id.item_multi_media_view_mask), with, with);
            imageViewMask.setBackgroundColor(mContext.getResources().getColor(R.color.color_ad_75000000));
            imageViewMask.setVisibility(item.isSelector() ? View.VISIBLE : View.GONE);
            Glide
                    .with(mContext)
                    .load(Uri.fromFile(new File(item.getFilePath())))
                    .into(imageView);
            helper.setGone(R.id.item_mutli_media_img_play, ADCursorManageUtils.ContentType.video == item.getContentType())
                    .setGone(R.id.item_multi_media_tv_duration, ADCursorManageUtils.ContentType.video == item.getContentType())
                    .setText(R.id.item_multi_media_tv_duration, 0 == item.getDuration() && item.getContentType() == ADCursorManageUtils.ContentType.video ? timeFormatUtils.formatDurations((int) (adCursorManageUtils.getMediaDuration(item.getFilePath()) / 1000)) : timeFormatUtils.formatDurations((int) item.getDuration() / 1000))
                    .setImageResource(R.id.item_multi_media_img_check_mark, item.isSelector() ? R.drawable.mis_btn_selected : R.drawable.mis_btn_unselected)
                    .setGone(R.id.item_multi_media_view_mask, item.isSelector());
        }
    }

    /**
     * 选中媒体文件
     *
     * @param mediaModel 选中数据
     * @param position   索引
     */
    public void setSelector(ADCursorManageUtils.ImageFolderModel.MediaModel mediaModel, int position) {
        getData().get(position).setSelector(!mediaModel.isSelector());
        notifyDataSetChanged();
    }

    public void set(ADCursorManageUtils.ImageFolderModel imageFolderModel, List<ADCursorManageUtils.ImageFolderModel.MediaModel> selectorMediaModel, boolean showCamera) {
        for (int i = 0; i < imageFolderModel.getMediaPath().size(); i++) {
            for (int j = 0; j < selectorMediaModel.size(); j++) {
                if (imageFolderModel.getMediaPath().get(i).getFilePath().equals(selectorMediaModel.get(j).getFilePath())) {
                    imageFolderModel.getMediaPath().get(i).setSelector(true);
                }
            }
        }
        if (showCamera) {
            imageFolderModel.getMediaPath().add(0, new ADCursorManageUtils.ImageFolderModel.MediaModel(
                    -1, "", "", 0, 0, ADCursorManageUtils.ContentType.image, 0, 0, "", 0
            ));
        }
        setNewData(imageFolderModel.getMediaPath());
    }
}
