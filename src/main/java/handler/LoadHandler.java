package handler;

import com.sun.net.httpserver.HttpExchange;
import request.LoadRequest;
import request.LoginRequest;
import result.LoginResult;
import result.Result;
import service.LoadService;
import service.LoginService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class LoadHandler extends Handler {
    private static Logger logger = Logger.getLogger("LoginHandler");

    LoadService service;
    Result result;
    LoadRequest loadRequest;


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            setExchange(exchange);

            if (isPostRequest()) {
                logger.info("THIS IS A POST REQUEST!");

                String reqData = getRequest();

                loadRequest = gson.fromJson(reqData, LoadRequest.class);

                service = new LoadService();
                result = service.processRequest(loadRequest);

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
