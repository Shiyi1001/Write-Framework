package com.shiyi.io;

import java.io.InputStream;

/**
 * @ClassName Resources
 * @Description
 * @Author FengL
 * @Date 2021/06/17 15:04
 * @Version V1.0
 */
public class Resources {

    /**
     * 根据配置文件的路径，将配置文件加载成字节流，存储在内存中
     * @Param path
     * @Return java.io.InputStream
     * @Date 2021/06/17 15:08
     * @Author FengL
     */
    public static InputStream getResourceAsSteam(String path){

        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
