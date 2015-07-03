package com.glodon.myapplication.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
/**
 * 强大的ViewHolder
 * @author jiax-a
 *
 */
public class ViewHolder {

    private View mConvertView;
    private int mPosition;
    //key：每个view对象的资源id。value:每个view对象
    private Map<Integer, View> mViews;

    private ViewHolder(ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        this.mViews = new HashMap<Integer, View>();
        mConvertView.setTag(this);
    }


    /**
     * 获取一个ViewHolder
     *
     * @param parent      ViewGroup对象，方便获取context，去初始化布局
     * @param convertView
     * @param layoutId
     * @return
     */
    public static ViewHolder get(ViewGroup parent, View convertView, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }


    @SuppressWarnings("unchecked")
    /**
     * 实例化布局中每个view对象
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public View getConvertView() {
        return mConvertView;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
