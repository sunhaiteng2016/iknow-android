package com.beyond.popscience.frame.base;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.beyond.popscience.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseAdapter
 * @author linjinfa@126.com
 * @date 2013-4-17 上午10:03:18 
 */
public abstract class CustomRecyclerBaseAdapter<T> extends RecyclerView.Adapter{

	protected List<T> dataList;
	protected LayoutInflater inflater;
	protected Activity context;
	protected Fragment fragment;
    private DisplayImageOptions displayImageOptions;
    private DisplayImageOptions avatarDisplayImageOptions;
    /**
     *
     */
    protected OnItemClickListener onItemClickListener;

	public CustomRecyclerBaseAdapter(Activity context) {
		super();
		this.context = context;
		init();
	}

	public CustomRecyclerBaseAdapter(Fragment fragment) {
		super();
		this.fragment = fragment;
		this.context = fragment.getActivity();
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init(){
		dataList = new ArrayList<T>();
		inflater = LayoutInflater.from(context);
	}

    /**
     *
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions(){
        if(displayImageOptions==null || displayImageOptions.getPreProcessor()!=null){
            displayImageOptions = getBaseDisplayImageOptions().build();
        }
        return displayImageOptions;
    }

    /**
     *
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions(int defaultResId){
        if(displayImageOptions==null || displayImageOptions.getPreProcessor()!=null){
            if(defaultResId>0){
                displayImageOptions = getBaseDisplayImageOptions().showImageOnLoading(defaultResId).showImageForEmptyUri(defaultResId).build();
            }else{
                displayImageOptions = getBaseDisplayImageOptions().build();
            }
        }
        return displayImageOptions;
    }

    /**
     *
     * @return
     */
    protected DisplayImageOptions.Builder getBaseDisplayImageOptions(){
        return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_bg_img_loading).showImageOnFail(R.drawable.default_bg_img_loading).showImageForEmptyUri(R.drawable.default_bg_img_loading).displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true);
    }

    /**
     * 头像
     * @return
     */
    protected DisplayImageOptions getAvatarDisplayImageOptions(){
        if(avatarDisplayImageOptions == null){
            avatarDisplayImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_header_icon).showImageOnFail(R.drawable.default_header_icon).showImageForEmptyUri(R.drawable.default_header_icon).displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).build();
        }
        return avatarDisplayImageOptions;
    }

	@Override
	public int getItemCount() {
		return dataList.size();
	}

    public T getItem(int position) {
        if(position<0 || position>=dataList.size()){
            return null;
        }
        return dataList.get(position);
    }
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<T> getDataList(){
		return dataList;
	}

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     *
     */
    public interface OnItemClickListener{

        void onItemClick(RecyclerView.ViewHolder holder, int position, long id);
    }
	
}
