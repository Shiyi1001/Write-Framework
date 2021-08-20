package server;

import java.io.IOException;

/**
 * @ClassName Servlet
 * @Description
 * @Author FengL
 * @Date 2021/08/19 16:08
 * @Version V1.0
 */
public interface Servlet {

    void init() throws IOException;

    void destory() throws IOException;

    void service(Request request, Response response) throws IOException;
}
