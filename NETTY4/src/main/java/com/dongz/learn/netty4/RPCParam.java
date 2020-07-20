package com.dongz.learn.netty4;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 16:03
 */
@Data
public class RPCParam implements Serializable {
    private String clazzName;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramTypes;

    public void initParams(int size){
        params = new Object[size];
        paramTypes = new Class[size];
    }
}
