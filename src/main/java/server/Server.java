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

        logger.info("Creating contexts");

        server.createContext("/user/register", new RegisterHandler());

        server.createContext("/user/login", new LoginHandler());

        server.createContext("/fill/", new FillHandler());

        server.createContext("/clear", new ClearHandler());

        server.createContext("/load", new LoadHandler());

        server.createContext("/person/", new PersonHandler());

        server.createContext("/person", new AllPersonHandler());

        server.createContext("/event/", new EventHandler());

        server.createContext("/event", new AllEventHandler());

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
