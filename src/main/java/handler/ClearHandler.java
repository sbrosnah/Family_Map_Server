package handler;

import com.sun.net.httpserver.HttpExchange;
import result.Result;
import service.ClearService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ClearHandler extends Handler {
    private static Logger logger = Logger.getLogger("ClearHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            setExchange(exchange);

            if (isPostRequest()) {
                logger.info("THIS IS A POST REQUEST!");

                ClearService service = new ClearService();
                Result result = service.processRequest();

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
