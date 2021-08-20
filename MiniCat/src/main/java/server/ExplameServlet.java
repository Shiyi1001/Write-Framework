package server;

import java.io.IOException;

/**
 * @ClassName ExplameServlet
 * @Description
 * @Author FengL
 * @Date 2021/08/19 16:13
 * @Version V1.0
 */
public class ExplameServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = "<h1>Servlet get</h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>Servlet post</h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws IOException {

    }

    @Override
    public void destory() throws IOException {

    }
}
