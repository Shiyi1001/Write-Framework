package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName Response
 * @Description 封装Response对象
 * @Author FengL
 * @Date 2021/08/19 10:09
 * @Version V1.0
 */
public class Response {


    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 输出指定字符串
     *
     * @Param content
     * @Return void
     * @Date 2021/08/19 10:11
     * @Author FengL
     */
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    /**
     * @Param path 根据url 获取静态资源的绝对路径，根据绝对路径获取静态资源文件，进行输出
     * @Return void
     * @Date 2021/08/19 10:13
     * @Author FengL
     */
    public void outputHtml(String path) throws IOException {

        //获取静态文件的绝对路径
        String absolutePath = StaticResourceUtil.getAbsolutePath(path);

        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            //读取静态文件，输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else{
            //输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
