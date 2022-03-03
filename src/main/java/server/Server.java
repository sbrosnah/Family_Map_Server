package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.logging.Logger;

import handler.HandlerType;
import handler.FileHandler;
import handler.RegisterHandler;
import handler.AllPersonHandler;
import handler.AllEventHandler;
import handler.ClearHandler;
import handler.EventHandler;
import handler.FillHandler;
import handler.LoadHandler;
import handler.LoginHandler;
import handler.PersonHandler;

public class Server {

    private static Logger logger = Logger.getLogger("Server");

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    private void run(String portNumber) {

        logger.setUseParentHandlers(true);
        logger.info("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        //TODO: create javadoc comments
        logger.info("Creating contexts");

        /**
         * This creates the /user/register context and sends the request to the RegisterHandler()
         */
        server.createContext("/user/register", new RegisterHandler());

        //TODO: Implement all of the handlers and uncomment the contexts

        server.createContext("/user/login", new LoginHandler());
        /*
        server.createContext("/clear", new ClearHandler());

        //TODO: figure out how to pass in the username and generations through the URL
        server.createContext("/user/[username]/{generations}", new FillHandler());

        server.createContext("/load", new LoadHandler());

        server.createContext("/person/[personID]", new PersonHandler());

        server.createContext("/person", new AllPersonHandler());

        server.createContext("/event/[eventID]", new EventHandler());

        server.createContext("/event", new AllEventHandler());
        */

        server.createContext("/", new FileHandler());

        logger.info("Starting server");

        server.start();

        logger.info("Server started");
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
