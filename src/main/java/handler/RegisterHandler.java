package handler;

import com.sun.net.httpserver.HttpExchange;
import java.net.*;
import java.io.*;
import java.util.logging.Logger;

import request.RegisterRequest;
import service.RegisterService;
import result.RegisterResult;

public class RegisterHandler extends Handler{
    private static Logger logger = Logger.getLogger("RegisterHandler");


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            setExchange(exchange);

            if (isPostRequest()) {
                logger.info("THIS IS A POST REQUEST!");


                String reqData = getRequest();

                RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                RegisterService service = new RegisterService();
                RegisterResult result = service.processRequest(request);

                logger.info(result.toString());

                if(result.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }


                String json = gson.toJson(result);

                sendResponse(json);

                success = true;
            }

            if (!success) {
                sendBadRequestResponse();
            }
        } catch (IOException e) {
            sendInternalErrorResponse(e);
        }
    }
}
