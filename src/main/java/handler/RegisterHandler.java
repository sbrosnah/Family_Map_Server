package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.google.gson.Gson;

import request.*;
import service.*;
import result.*;

public class RegisterHandler implements HttpHandler {
    private static Logger logger = Logger.getLogger("RegisterHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                logger.info("THIS IS A POST REQUEST!");
                //Instantiate the Gson object
                Gson gson = new Gson();

                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                RegisterRequest request = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);

                RegisterService service = new RegisterService();
                RegisterResult result = (RegisterResult) service.processRequest(request);

                logger.info(result.toString());

                if(result.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream resBody = exchange.getResponseBody();
                String json = gson.toJson(result);
                resBody.write(json.getBytes(StandardCharsets.UTF_8));
                exchange.getResponseBody().close();
                resBody.close();

                success = true;
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
