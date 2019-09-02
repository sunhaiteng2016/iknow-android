package com.beyond.popscience.module.home.entity;

public class HomeMessage {

    public static class homeRefresh {
        public int type;//1向下 2 向上
        public int headTop;//1向下 2 向上

        public homeRefresh(int headviewHight, int headTop) {
            this.type = headviewHight;
            this.headTop = headTop;

        }
    }
    public static class menuRefresh {}
    public static class isXianshi{}
}
