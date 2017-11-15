package Server.Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import Facade.Request.PersonRequest;
import Facade.Result.PersonResult;
import Facade.Service.PersonService;
import Server.JSONEncoder;

import static java.net.HttpURLConnection.*;

/**
 * PERSON HANDLER:
 * The handler for person requests.
 */
public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /person handler");

        boolean success = false;

        try {
            // get request?
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                // get request headers
                Headers request_headers = exchange.getRequestHeaders();

                // has auth token?
                if(request_headers.containsKey("Authorization")) {
                    final int NO_PARAMS_LENGTH = 2;
                    final int PERSON_ID_LENGTH = 3;
                    final int PERSON_ID_INDEX = 2;
                    String token;
                    String[] params;

                    // extract token (and parameter)
                    token = request_headers.getFirst("Authorization");
                    params = exchange.getRequestURI().getPath().split("/");

                    if(params.length == NO_PARAMS_LENGTH || params.length == PERSON_ID_LENGTH) {
                        PersonRequest person_request;
                        String person_id;
                        PersonResult person_result;
                        String response_json;
                        OutputStream response_body;

                        // /person
                        if(params.length == NO_PARAMS_LENGTH) {
                            person_request = new PersonRequest(token);

                        // /person/[person_id]
                        } else {
                            person_id = params[PERSON_ID_INDEX];
                            person_request = new PersonRequest(token, person_id);
                        }
                        System.out.println(JSONEncoder.encodeObject(person_request));

                        // execute person request
                        person_result = PersonService.getInstance().getPerson(person_request);

                        // send response
                        exchange.sendResponseHeaders(HTTP_OK, 0);
                        response_json = JSONEncoder.encodeObject(person_result);
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
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
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