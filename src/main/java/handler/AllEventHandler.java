package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import result.AllEventResult;
import service.AllEventService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class AllEventHandler extends Handler {
    private static Logger logger = Logger.getLogger("PersonHandler");
    AllEventService allEventService;
    AllEventResult result;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            setExchange(exchange);
            if(isGetRequest()){
                Headers reqHeaders = exchange.getRequestHeaders();
                if(reqHeaders.containsKey("Authorization")){
                    String authtoken = reqHeaders.getFirst("Authorization");

                    allEventService = new AllEventService();

                    result = allEventService.processRequest(authtoken);

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
            if(!success){
                sendBadRequestResponse();
            }
        } catch (IOException e) {
            sendInternalErrorResponse(e);
        }
    }
}
