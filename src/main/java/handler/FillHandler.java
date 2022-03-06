package handler;

import com.sun.net.httpserver.HttpExchange;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import service.FillService;
import result.Result;

public class FillHandler extends Handler {
    private static Logger logger = Logger.getLogger("FillHandler");


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            setExchange(exchange);

            if (isPostRequest()) {
                logger.info("THIS IS A POST REQUEST!");

                String URI = exchange.getRequestURI().toString();
                String[] tokens = URI.split("/");
                int numTokens = tokens.length;

                FillService service = new FillService();
                Result result;
                if(numTokens <= 3){
                    result = service.processRequest(tokens[2], null);
                } else {
                    result = service.processRequest(tokens[2], Integer.parseInt(tokens[3]));
                }

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
