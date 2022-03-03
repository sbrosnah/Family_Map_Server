package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    protected Gson gson ;
    protected InputStream reqData;
    private HandlerType handlerType;
    private HttpExchange exchange;

    Handler(){
        gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException{}

    protected String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    protected void setExchange(HttpExchange exchange){
        this.exchange = exchange;
    }

    protected boolean isPostRequest(){
        return exchange.getRequestMethod().toLowerCase().equals("post");
    }

    protected boolean isGetRequest() {return exchange.getRequestMethod().toLowerCase().equals("get"); }

    protected String getRequest() throws IOException {
        // Get the request body input stream
        InputStream reqBody = exchange.getRequestBody();

        // Read JSON string from the input stream
        String reqData = readString(reqBody);

        return reqData;
    }

    protected void sendResponse(String json) throws IOException{
        OutputStream resBody = exchange.getResponseBody();
        resBody.write(json.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
        resBody.close();
    }

    protected void sendBadRequestResponse() throws IOException {
        // The HTTP request was invalid somehow, so we return a "bad request"
        // status code to the client.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

        // We are not sending a response body, so close the response body
        // output stream, indicating that the response is complete.
        exchange.getResponseBody().close();
    }

    protected void sendInternalErrorResponse(IOException e) throws IOException{
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
