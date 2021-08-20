package server;

import java.io.IOException;

/**
 * @ClassName HttpServlet
 * @Description
 * @Author FengL
 * @Date 2021/08/19 16:10
 * @Version V1.0
 */
public abstract class HttpServlet implements Servlet {

    public abstract void doGet(Request request, Response response);
    public abstract void doPost(Request request, Response response);

    @Override
    public void service(Request request, Response response) throws IOException {
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
