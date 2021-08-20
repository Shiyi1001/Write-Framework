package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName StaticResourceUtil
 * @Description
 * @Author FengL
 * @Date 2021/08/19 10:15
 * @Version V1.0
 */
public class StaticResourceUtil {

    /**
     * 获取静态资源的绝对路径
     *
     * @Param path
     * @Return java.lang.String
     * @Date 2021/08/19 10:16
     * @Author FengL
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 读取静态文件输入流，通过输出流输出
     *
     * @Param inputStream
     * @Param outputStream
     * @Return void
     * @Date 2021/08/19 10:22
     * @Author FengL
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {

        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        System.out.println(count);
        //输出请求头
        outputStream.write(HttpProtocolUtil.getHttpHeader200(count).getBytes());

        //读取内容
        byte[] bytes = new byte[1024];
        int len;

        while ((len = inputStream.read(bytes,0,bytes.length)) != -1) {
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }


    }
}
