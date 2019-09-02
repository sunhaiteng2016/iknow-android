/*
 * Copyright (C) 2010-2013 The WEIMOB Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beyond.popscience.frame.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.library.network.NetWorkInject;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.library.network.task.IUIController;
import com.beyond.library.network.task.TaskManager;
import com.beyond.popscience.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public abstract class BaseFragment extends Fragment implements IUIController {

    protected View mRootView;
    private boolean isSwitchFragmenting = false;
    private Unbinder unbinder;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        TaskManager.getInstance().registerUIController(this);
        NetWorkInject.init(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResID(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    protected abstract int getLayoutResID();

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        dismissProgressDialog();
        TaskManager.getInstance().unRegisterUIController(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();

    }

    @Override
    public void initUI() {

    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        if (getActivity() instanceof BaseActivity) {
            if (((BaseActivity) getActivity()).dealMSG(msg)) {
                return;
            }
        }
    }

    @Override
    public String getIdentification() {
        return getClass().toString() + this;
    }

    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialog() {
        showProgressDialog("加载中...");
    }

    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialog(String message) {
        showProgressDialog(message, true);
    }

    /**
     * 显示Progress
     *
     * @param isCancelable 返回键是否可以dismiss
     */
    protected void showProgressDialog(String message, boolean isCancelable) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog(message, isCancelable);
        }
    }

    /**
     * 关闭Progress
     */
    protected void dismissProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissProgressDialog();
        }
    }

    /**
     * 执行Task任务
     *
     * @param task
     */
    protected void execuTask(ITask task) {
        if (task == null)
            return;
        task.setContext(getActivity());
        task.setmIdentification(getIdentification());
        TaskManager.getInstance().addTask(task);
    }

    /**
     * 替换Fragment (默认有动画效果)
     *
     * @param resLayId
     * @param fragment
     * @param isAddBackStack 是否加入返回栈
     */
    protected void replaceFragment(int resLayId, Fragment fragment,
                                   boolean isAddBackStack) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getChildFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

    /**
     * 替换Fragment
     *
     * @param resLayId
     * @param fragment
     * @param isAddBackStack
     * @param isAnimation
     */
    protected void replaceFragment(int resLayId, Fragment fragment,
                                   boolean isAddBackStack, boolean isAnimation) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getChildFragmentManager()
                .beginTransaction();
        if (isAnimation)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

}
