package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName Request
 * @Description
 * @Author FengL
 * @Date 2021/08/18 17:29
 * @Version V1.0
 */
public class Request {

    private String url;
    private String method;

    private InputStream inputStream;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        //从输入流中获取请求信息
        int count = 0;
        while(count == 0){
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String str = new String(bytes);

        //获取第一行请求头  GET / HTTP/1.1
        String firstLine = str.split("\\n")[0];
        String[] strings = firstLine.split(" ");

        this.method = strings[0];
        this.url = strings[1];

        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);

    }
}
