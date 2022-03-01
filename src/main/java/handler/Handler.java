package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import service.Service;
import request.Request;
import result.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Handler implements HttpHandler {
    private static Logger logger = Logger.getLogger("Handler");
    HandlerType handlerType;

    public Handler(HandlerType handlerType){
        this.handlerType = handlerType;
    }


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

                Request request = null;
                Service service = null;
                Result result = null;
                result = castToolsAndRunService(request, service, result, gson, reqData);

                logger.info("Attempting to print result");
                logger.info(result.toString());
                logger.info("successfully printed result");

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
            } else if (exchange.getRequestMethod().toLowerCase().equals("get")){
                logger.info("THIS IS A GET REQUEST!");

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

    private Result castToolsAndRunService(Request request, Service service, Result result, Gson gson, String reqData){
        logger.info("In castToolsAndRunService");
        if(handlerType.equals(HandlerType.REGISTER)){
            request = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);
            logger.info("Printing request: " + request.toString());
            service = new RegisterService();
            result = (RegisterResult) service.processRequest(request);
            logger.info("Printing result: " + result.toString());
        }
        return result;
    }

}
