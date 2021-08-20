package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName Bootstrap
 * @Description
 * @Author FengL
 * @Date 2021/08/18 17:09
 * @Version V1.0
 */
public class Bootstrap {

    private int port = 8089;

    private void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("MiniCat start port:" + port);

         /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        /*while (true) {
            Socket socket = serverSocket.accept();
            //接收到请求获取输入流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello MiniCat!";
            String reponseText = HttpProtocolUtil.getHttpHeader200(data.length()) + data;
            outputStream.write(reponseText.getBytes());
            socket.close();
        }*/

        /*
            完成Minicat 2.0版本
            需求：封装Request和Response对象，返回html静态资源文件
         */
/*        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());

            socket.close();
        }*/

        loadServlet();

/*        while (true) {
            Socket socket = serverSocket.accept();
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
        }*/

        //使用多线程
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50), new ThreadPoolExecutor.AbortPolicy());

        while (true) {
            Socket socket = serverSocket.accept();
            threadPoolExecutor.execute(new RequestProcessor(socket, servletMap));
        }

    }

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 解析web.xml 初始化servlet
     *
     * @Param
     * @Return void
     * @Date 2021/08/19 16:19
     * @Author FengL
     */
    private void loadServlet() {

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (Element element : selectNodes) {
                // <servlet-name>explame</servlet-name>
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();

                //<servlet-calss>server.ExplameServlet</servlet-calss>
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("//url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
