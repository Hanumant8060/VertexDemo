package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        List<Employee> employees = new ArrayList<>();
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Route posthandler = router.post("/addEmployee").handler(BodyHandler.create()).handler(routingContext -> {
            final Employee employee = Json.decodeValue(routingContext.getBody(), Employee.class);
            HttpServerResponse serverResponse = routingContext.response();
            serverResponse.setChunked(true);
            employees.add(employee);
            serverResponse.end(employees.size() + " employee added successfully");
        });
        Route getHandler = router.get("/getEmployees").produces("*/json").handler(routingContext->{
            routingContext.response().setChunked(true).end(Json.encodePrettily(employees));
        });
        Route getFilterHandler = router.get("/getEmployee/:name").produces("*/json").handler(routingContext->{
            String name = routingContext.request().getParam("name");
            routingContext.response().setChunked(true).end(Json.encodePrettily(employees.stream().filter(emp->emp.getName().equals(name)).findAny().get()));
        });
        httpServer
                .requestHandler(router::accept)
                .listen(8091);
        System.out.println("Hello world.....");
    }


    }

