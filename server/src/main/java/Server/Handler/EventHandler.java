package Server.Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Facade.Request.EventRequest;
import Facade.Result.EventResult;
import Facade.Service.EventService;
import Server.JSONEncoder;

import static java.net.HttpURLConnection.*;

/**
 * EVENT HANDLER:
 * The handler for event requests.
 */
public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /event handler");

        boolean success = false;

        try {
            // get request?
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                // get request headers
                Headers request_headers = exchange.getRequestHeaders();

                // has auth token?
                if(request_headers.containsKey("Authorization")) {
                    final int NO_PARAMS_LENGTH = 2;
                    final int EVENT_ID_LENGTH = 3;
                    final int EVENT_ID_INDEX = 2;
                    String token;
                    String[] params;

                    // extract token (and parameter)
                    token = request_headers.getFirst("Authorization");
                    params = exchange.getRequestURI().getPath().split("/");

                    if(params.length == NO_PARAMS_LENGTH || params.length == EVENT_ID_LENGTH) {
                        EventRequest event_request;
                        String event_id;
                        EventResult event_result;
                        String response_json;
                        OutputStream response_body;

                        // /event
                        if(params.length == NO_PARAMS_LENGTH) {
                            event_request = new EventRequest(token);

                            // /event/[event_id]
                        } else {
                            event_id = params[EVENT_ID_INDEX];
                            event_request = new EventRequest(token, event_id);
                        }
                        System.out.println(JSONEncoder.encodeObject(event_request));

                        // execute event request
                        event_result = EventService.getInstance().getEvent(event_request);

                        // send response
                        exchange.sendResponseHeaders(HTTP_OK, 0);
                        response_json = JSONEncoder.encodeObject(event_result);
                        response_body = exchange.getResponseBody();
                        stringToStream(response_json, response_body);

                        // complete exchange
                        response_body.close();
                        success = true;
                        System.out.println();
                    }
                }
            }

            // unsuccessful?
            if(!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }

        } catch(IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
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