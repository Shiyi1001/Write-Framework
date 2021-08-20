package server;

/**
 * @ClassName HttpProtocolUtil
 * @Description http协议工具类，主要是提供响应头信息，这里我们只提供200和404的情况
 * @Author FengL
 * @Date 2021/08/18 17:15
 * @Version V1.0
 */
public class HttpProtocolUtil {

    /**
     *  响应码200提供请求头信息
     * @Param contentLength 返回信息长度
     * @Return java.lang.String
     * @Date 2021/08/18 17:23
     * @Author FengL
     */
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + contentLength + " \n" +
                "\r\n";
    }

    /**
     * 为响应码404提供请求头信息(此处也包含了数据内容)
     * @Param
     * @Return java.lang.String
     * @Date 2021/08/18 17:23
     * @Author FengL
     */
    public static String getHttpHeader404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + str404.getBytes().length + " \n" +
                "\r\n" + str404;
    }

}
