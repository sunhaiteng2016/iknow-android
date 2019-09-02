package com.beyond.popscience.frame.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;

/**
 * 发表评论View
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class PublishedCommentView extends RelativeLayout {

    private View bgView;
    private EditText commentEditTxt;
    private OnPublishedClickListener onPublishedOnClickListener;

    public PublishedCommentView(Context context) {
        super(context);
        init();
    }

    public PublishedCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PublishedCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        setVisibility(View.INVISIBLE);
        View.inflate(getContext(), R.layout.dialog_published_comment, this);

        bgView = findViewById(R.id.bgView);
        commentEditTxt = (EditText) findViewById(R.id.commentEditTxt);
        TextView cancelCommentTxtView = (TextView) findViewById(R.id.cancelCommentTxtView);
        TextView publishedTxtView = (TextView) findViewById(R.id.publishedTxtView);
        cancelCommentTxtView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPublishedOnClickListener!=null){
                    onPublishedOnClickListener.onCancel();
                }
                hidden();
            }
        });
        publishedTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPublishedOnClickListener!=null){
                    onPublishedOnClickListener.onOk();
                }
            }
        });
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPublishedOnClickListener!=null){
                    onPublishedOnClickListener.onCancel();
                }
                hidden();
            }
        });
    }

    /**
     * 显示
     */
    public void show(){
        showSoftInput();
        bgView.setAlpha(0f);
        bgView.animate().alpha(1f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setVisibility(View.VISIBLE);
            }
        }).start();
    }

    /**
     * 隐藏
     */
    public void hidden(){
        hiddenSoftInput();

        bgView.setAlpha(1f);
        bgView.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(View.INVISIBLE);
            }
        }).start();
    }

    /**
     * 显示软键盘 并绑定到指定View
     */
    public void showSoftInput(){
        commentEditTxt.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(commentEditTxt, InputMethodManager.RESULT_SHOWN);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 通过绑定的View 隐藏软键盘
     */
    public void hiddenSoftInput(){
        commentEditTxt.clearFocus();
        if(commentEditTxt.getWindowToken()!=null){
            try {
                InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(commentEditTxt.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否正在显示
     */
    public boolean isShow(){
        return getVisibility() == View.VISIBLE;
    }

    /**
     *
     * @return
     */
    public String getText(){
        return commentEditTxt.getText().toString();
    }

    /**
     *
     * @param content
     */
    public void setText(String content){
        commentEditTxt.setText(content);
    }

    public OnPublishedClickListener getOnPublishedOnClickListener() {
        return onPublishedOnClickListener;
    }

    public void setOnPublishedOnClickListener(OnPublishedClickListener onPublishedOnClickListener) {
        this.onPublishedOnClickListener = onPublishedOnClickListener;
    }

    private interface OnPublishedClickListener{
        /**
         *
         */
        void onOk();

        /**
         *
         */
        void onCancel();
    }

    public static class SimplePublishedClickListener implements OnPublishedClickListener{

        @Override
        public void onOk() {

        }

        @Override
        public void onCancel() {

        }
    }

}
