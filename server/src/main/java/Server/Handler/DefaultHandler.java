package Server.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.*;

/**
 * FILE HANDLER:
 * The handler for all default requests.
 */
public class DefaultHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: / handler");

        final String DEFAULT_PATH = "server/src/main/res/web";
        final String NOT_FOUND_PATH = DEFAULT_PATH + "/HTML/404.html";
        OutputStream response_body;
        byte[] file;

        try {
            // get request?
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String path_to_file;

                // get path to file
                path_to_file = exchange.getRequestURI().getPath();

                // index.html?
                if (path_to_file.equals("/") || path_to_file.equals("/index.html")) {
                    path_to_file = DEFAULT_PATH + "/index.html";

                    // other file
                } else {
                    path_to_file = DEFAULT_PATH + path_to_file;
                }

                // read file
                file = Files.readAllBytes(Paths.get(path_to_file));

                // send response
                exchange.sendResponseHeaders(HTTP_OK, 0);
                response_body = exchange.getResponseBody();
                response_body.write(file);

                // complete exchange
                response_body.close();
            }

        } catch(Exception e) {
            // send header
            exchange.sendResponseHeaders(HTTP_NOT_FOUND, 0);

            // get 404 file
            file = Files.readAllBytes(Paths.get(NOT_FOUND_PATH));
            response_body = exchange.getResponseBody();
            response_body.write(file);

            // send body
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}