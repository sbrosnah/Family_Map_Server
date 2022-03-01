package handler;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath == null || urlPath.equals("/")){
                    urlPath = "/index.html";
                    String filePath = "web" + urlPath;
                    File file = new File(filePath);
                    if(file.exists()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        Files.copy(file.toPath(), respBody);
                        respBody.close();
                        success = true;
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                        exchange.getResponseBody().close();
                    }
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Since the client request was invalid, they will not receive the
                // list of games, so we close the response body output stream,
                // indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        } catch(IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }
}
