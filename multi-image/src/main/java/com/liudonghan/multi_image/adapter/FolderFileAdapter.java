package com.liudonghan.multi_image.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liudonghan.multi_image.R;
import com.liudonghan.utils.ADCursorManageUtils;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/12/23
 */
public class FolderFileAdapter extends BaseQuickAdapter<ADCursorManageUtils.ImageFolderModel, BaseViewHolder> {

    public FolderFileAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ADCursorManageUtils.ImageFolderModel item) {
        helper.setText(R.id.item_folder_file_tv_name, item.getDirName())
                .setText(R.id.item_folder_file_tv_path, item.getDirPath())
                .setText(R.id.item_folder_file_tv_count, item.getFileCount() + "张")
                .setGone(R.id.item_folder_file_img_indicator, item.isSelect());
        Glide.with(mContext).load(item.getCoverPath()).into((ImageView) helper.getView(R.id.item_folder_file_img_cover));
    }

    /**
     * 设置选中
     *
     * @param imageFolderModel 选中数据
     */
    public void setSelector(ADCursorManageUtils.ImageFolderModel imageFolderModel) {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getDirName().equals(imageFolderModel.getDirName())) {
                getData().get(i).setSelect(true);
            } else {
                getData().get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }
}
