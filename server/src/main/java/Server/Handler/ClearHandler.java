package Server.Handler;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import Facade.Result.ClearResult;
import Facade.Service.ClearService;
import Server.JSONEncoder;

import static java.net.HttpURLConnection.*;

public class ClearHandler implements HttpHandler {
// METHODS
    /**
     * HANDLE:
     * Handles a clear request.
     * @param  exchange    [description]
     * @throws IOException [description]
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /clear handler");

        boolean success = false;

        try {
            // POST request?
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                ClearResult clear_result;
                String response_json;

                // clear database
                clear_result = ClearService.getInstance().clearDatabase();

                // send response
                exchange.sendResponseHeaders(HTTP_OK, 0);
                response_json = JSONEncoder.encodeObject(clear_result);
                OutputStream response_body = exchange.getResponseBody();
                stringToStream(response_json, response_body);

                // complete exchange
                response_body.close();
                success = true;
                System.out.println();
            }

            // unsuccessful
            if(!success) {
                // return bad request
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

                // response complete
                exchange.getResponseBody().close();
            }

        } catch(IOException e) {
            exchange.sendResponseHeaders(HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * STRING TO STREAM:
     * Writes a string into an output stream.
     *
     * @param str, a non-empty string
     * @param os, an instantiated output stream object
     */
    private void stringToStream(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}