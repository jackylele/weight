package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletContextServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        //http://localhost:8080/getWeight
        context.addServlet(new ServletHolder(new WeightServlet()), "/getWeight");
        Thread thread = new GetWeight();
        thread.start();

        server.start();
        server.join();
    }
}