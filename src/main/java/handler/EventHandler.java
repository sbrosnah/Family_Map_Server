package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import service.EventService;
import result.EventResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class EventHandler extends Handler {
    private static Logger logger = Logger.getLogger("EventHandler");
    EventService eventService;
    EventResult result;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            setExchange(exchange);
            if(isGetRequest()) {
                logger.info("THIS IS A GET REQUEST");
                Headers reqHeaders = exchange.getRequestHeaders();
                if(reqHeaders.containsKey("Authorization")){
                    String authtoken = reqHeaders.getFirst("Authorization");

                    String URI = exchange.getRequestURI().toString();
                    String[] tokens = URI.split("/");
                    int numTokens = tokens.length;

                    eventService = new EventService();

                    if(numTokens == 3) {

                        result = eventService.processRequest(authtoken, tokens[2]);

                        if(result.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        String json = gson.toJson(result);

                        sendResponse(json);

                        success = true;
                    }
                }

            }
            if(!success) {
                sendBadRequestResponse();
            }
        } catch (IOException e) {
            sendInternalErrorResponse(e);
        }
    }
}
