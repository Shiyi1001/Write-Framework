package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @ClassName RequestProcessor
 * @Description
 * @Author FengL
 * @Date 2021/08/19 16:53
 * @Version V1.0
 */
public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String,HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            //静态资源处理
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                //处理动态资源
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
