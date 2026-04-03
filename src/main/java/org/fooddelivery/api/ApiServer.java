package org.fooddelivery.api;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.ws.transport.http.server.EndpointImpl;
import jakarta.xml.ws.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ApiServer {

    public static void startAll() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            publish(server, "/auth",            new AuthSoapService());
            publish(server, "/restaurants",     new RestaurantSearchService());
            publish(server, "/menu",            new MenuQueryService());
            publish(server, "/customer/orders", new CustomerOrderService());
            publish(server, "/owner/orders",    new OwnerOrderService());
            publish(server, "/payments",        new PaymentSoapService());
            publish(server, "/order/status",    new OrderStatusService());
            publish(server, "/rider/tasks",     new RiderTaskService()); 

            // Static UI
            server.createContext("/", ApiServer::serveStatic);

            server.start();
            System.out.println("SOAP + UI started at http://localhost:8080/");
        } catch (IOException e) {
            System.out.println("API Server error: " + e.getMessage());
        }
    }

    private static void publish(HttpServer server, String path, Object service) {
        HttpContext ctx = server.createContext(path);
        EndpointImpl ep = (EndpointImpl) Endpoint.create(service);
        ep.publish(ctx);
        ctx.getFilters().add(new CorsFilter());
    }

    public static class CorsFilter extends Filter {
        @Override public String description() { return "CORS"; }

        @Override
        public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers",
                    "Content-Type, Authorization, SOAPAction");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                try (exchange) {
                    exchange.sendResponseHeaders(204, -1);
                }
                return;
            }
            chain.doFilter(exchange);
        }
    }

    private static void serveStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";

        String contentType = path.endsWith(".js")  ? "application/javascript" :
                             path.endsWith(".css") ? "text/css" : "text/html";

        InputStream resource = ApiServer.class.getResourceAsStream("/web" + path);
        if (resource == null) {
            exchange.sendResponseHeaders(404, 0);
            exchange.getResponseBody().close();
            return;
        }

        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            resource.transferTo(os);
        }
    }
}