package com.beyond.library.network.task;


import com.beyond.library.network.enity.MSG;

import java.text.ParseException;

/**
 * 控制器接口
 * @author linjinfa@126.com
 * @date 2013-4-16 上午10:06:01
 */
public interface IUIController {
	
	/**
	 * 初始化控件
	 */
	public void initUI() throws ParseException;
	
	/**
	 * 指定刷新的控件，及返回结果
	 * @param taskId
	 * @param msg
	 */
	public void refreshUI(int taskId, MSG msg);

    /**
     * IUIController 的标识
     * @return
     */
    public String getIdentification();

}
