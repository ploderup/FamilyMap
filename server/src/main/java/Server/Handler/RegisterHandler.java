package Server.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static java.net.HttpURLConnection.*;

import Facade.Request.RegisterRequest;
import Facade.Result.RegisterResult;
import Facade.Service.RegisterService;
import Server.JSONDecoder;
import Server.JSONEncoder;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /user/register handler");

        boolean success = false;

        try {
            // post request?
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream request_body;
                String request_json;
                RegisterRequest register_request;
                RegisterResult register_result;
                String response_json;
                OutputStream response_body;

                // get request
                request_body = exchange.getRequestBody();
                request_json = streamToString(request_body);
                register_request = (RegisterRequest)JSONDecoder.decodeRequest(request_json,
                        RegisterRequest.class);
                System.out.println(request_json);

                // register user
                register_result = RegisterService.getInstance().registerUser(register_request);

                // send response
                exchange.sendResponseHeaders(HTTP_OK, 0);
                response_json = JSONEncoder.encodeObject(register_result);
                response_body = exchange.getResponseBody();
                stringToStream(response_json, response_body);

                // complete exchange
                response_body.close();
                success = true;
                System.out.println();
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
     * STREAM TO STRING:
     * Reads an input stream into a string.
     *
     * @param is, an instantiated input stream
     * @return a string
     * @throws IOException
     */
    private String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * STRING TO STREAM:
     * Writes a string into an output stream.
     *
     * @param str, a non-empty string
     * @param os, an instantiated output stream object
     * @throws IOException
     */
    private void stringToStream(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}