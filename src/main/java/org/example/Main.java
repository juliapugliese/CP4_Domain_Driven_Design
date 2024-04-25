package org.example;

import org.example.entities.Card;
import org.example.entities.Collection;
import org.example.repositories.CardRepository;
import org.example.repositories.CollectionRepository;
import org.example.repositories.Starter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.example package
        final ResourceConfig rc = new ResourceConfig().packages("org.example");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //teste


        new Starter().initialize();

        var coleRepo = new CollectionRepository();

        var cardRepo = new CardRepository();

        var carta = new Card(1, "Cathar's Call","Enchantment — Aura", "Enchanted creature has vigilance and “At the beginning of your end step, create a 1/1 white Human creature token.”", 3,2, 5.00);

        var carta2 = new Card(3, "snorlax","Enchantment — Aura", "Human creature token.”", 3,5, 20.00);

        var carta3 = new Card(2, "pikachu","choque do trovao", "animal.”", 2,2, 2.00);
        var collection = new Collection(1, "Dark", LocalDate.now(), new ArrayList<>(List.of(carta2)));

        var collecao2 = new Collection(2,"Light", LocalDate.now(), new ArrayList<>(List.of(carta3)));

        coleRepo.create(collection);
        coleRepo.create(collecao2);
        //colecao precisa ser criada antes da carta para que exista a chave estrangeira
        cardRepo.create(carta);
        cardRepo.create(carta3);
        cardRepo.create(carta2);
        cardRepo.getAll();

        coleRepo.getAll();






//        var collection = new Collection(1, "Innistrad: Midnight Hunt (MID)", LocalDate.now(), List.of(carta, carta));
//        System.out.println(collection);



//        final HttpServer server = startServer();
//        System.out.println(String.format("Jersey app started with endpoints available at "
//                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
//        System.in.read();
//        server.stop();

    }
}

