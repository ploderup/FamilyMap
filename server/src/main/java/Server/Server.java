package Server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import Server.Handler.*;

public class Server {
// CONSTRUCTORS
    /**
     * Default Constructor:
     * Constructs a server object. Note, given <code>main</code> is currently located in this class,
     * this won't ever get called.
     */
    public Server() {}


// METHODS
    /**
     * MAIN:
     * Starts a server on a given port.
     *
     * @param args, an array of non-empty strings containing a port number
     */
    public static void main(String[] args) throws Exception {
        int port_number;

        // check input
        if(args.length == 0) {
            System.out.println("Please provide the program a port number.");
        }
        try {
//          port_number = Integer.parseInt(args[0]);
            port_number = 8080;
        } catch(NumberFormatException e) {
            System.err.println("Invalid port number. Please try again.");
            return;
        }

        // start server
        new Server().run(port_number);
    }

    /**
     * RUN:
     * Register all handlers with the new server, and start it.
     */
    private void run(int port_number) throws Exception {
        System.out.println("Initializing HTTP Server.");
        System.out.println();

        // create the server
        final int MAX_WAITING_CONNECTIONS = 12;
        server = HttpServer.create(new InetSocketAddress(port_number), MAX_WAITING_CONNECTIONS);

        // set executor
        server.setExecutor(null);

        // create contexts
        System.out.println("Creating contexts.");
        server.createContext("/", new DefaultHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/user/register", new RegisterHandler());

        // start the server
        System.out.println("Starting server.");
        server.start();
        System.out.println("Server listening on port: " + port_number + ".");
        System.out.println();
    }


// MEMBERS
    /**
     * SERVER:
     * A HTTPServer object.
     */
    private static HttpServer server;
}