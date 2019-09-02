package com.beyond.library.network.net.bean.model;

/**
 * Created by East.K on 2016/12/15.
 */

public enum  Application {

        UNKNOWN("Unknown", 0),
        H5("H5", 0x100),
        PC("PC", 0x200),
        ANDROID("Android", 0xA00),
        iOS("iOS", 0xB00);

        private String name = null;
        private int type = 0;

        Application(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getType() {
            return type;
        }
}
