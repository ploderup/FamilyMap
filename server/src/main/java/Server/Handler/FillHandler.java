package Server.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import Facade.Request.FillRequest;
import Facade.Result.FillResult;
import Facade.Service.FillService;
import Server.JSONEncoder;

import static java.net.HttpURLConnection.*;

/**
 * FILL HANDLER:
 * The handler for fill requests.
 */
public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /fill handler");
        System.out.println();

        boolean success = false;

        try {
            // post request?
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                final int USERNAME_INDEX = 2;
                final int NUM_GENERATIONS_INDEX = 3;
                final int MAX_NUM_GENERATIONS = 10;
                String[] params;

                // get URI
                params = exchange.getRequestURI().getPath().split("/");

                // correct number of parameters?
                if(params.length == USERNAME_INDEX+1 || params.length == NUM_GENERATIONS_INDEX+1) {
                    FillRequest fill_request;
                    String username;
                    int num_generations;
                    FillResult fill_result;
                    String response_json;
                    OutputStream response_body;

                    // /fill/[username]
                    if (params.length == USERNAME_INDEX+1) {
                        username = params[USERNAME_INDEX];
                        fill_request = new FillRequest(username);

                    // /fill/[username]/[num_generations]
                    } else {
                        username = params[USERNAME_INDEX];
                        num_generations = Integer.parseInt(params[NUM_GENERATIONS_INDEX]);
                        if(num_generations > MAX_NUM_GENERATIONS) throw new NumberFormatException();
                        fill_request = new FillRequest(username, num_generations);
                    }
                    System.out.println(JSONEncoder.encodeObject(fill_request));

                    // fill database
                    fill_result = FillService.getInstance().fillDatabase(fill_request);

                    // send response
                    exchange.sendResponseHeaders(HTTP_OK, 0);
                    response_json = JSONEncoder.encodeObject(fill_result);
                    response_body = exchange.getResponseBody();
                    stringToStream(response_json, response_body);

                    // complete exchange
                    response_body.close();
                    success = true;
                    System.out.println();
                }
            }

            // unsuccessful?
            if(!success) {
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }

        } catch(IOException e) {
            exchange.sendResponseHeaders(HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch(NumberFormatException e) {
            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
            exchange.getRequestBody().close();
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