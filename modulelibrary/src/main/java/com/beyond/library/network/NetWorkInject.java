package com.beyond.library.network;

import android.support.annotation.NonNull;

import com.beyond.library.network.net.httpclient.BaseRestUsage;
import com.beyond.library.network.task.IUIController;

import java.lang.reflect.Field;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public final class NetWorkInject {

    /**
     *
     * @param t
     * @param <T>
     */
    public static <T extends IUIController>void init(@NonNull T t){
        Field[] fields = t.getClass().getDeclaredFields();
        if(fields!=null && fields.length>0){
            for(Field field : fields){
                try {
                    field.setAccessible(true);
                    Request request = field.getAnnotation(Request.class);
                    if(request!=null){
                        Object fieldObj = field.getType().newInstance();
                        ((BaseRestUsage)fieldObj).setIdentification(t.getIdentification());
                        field.set(t, fieldObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
