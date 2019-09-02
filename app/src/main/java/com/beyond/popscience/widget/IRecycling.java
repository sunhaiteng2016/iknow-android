package com.beyond.popscience.widget;

/**
 * 无限循环接口，实现PagerAdapter时实现这个接口，配合RecyclingCirclePagerIndicator使用
 *
 * @author yao.cui
 */
public interface IRecycling {

    /**
     * 获取实际数量
     *
     * @return
     */
    int getRealCount();

    /**
     * 获取实际位置
     *
     * @return
     */
    int getRealPosition(int position);

}