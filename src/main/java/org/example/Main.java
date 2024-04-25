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

        var carta = new Card(1,"Cathar's Call","Enchantment — Aura", "Enchanted creature has vigilance and “At the beginning of your end step, create a 1/1 white Human creature token.”", 3,2, 5.00);

        var carta2 = new Card(2,"Panglacial Wurm","Creature — Wurm", "While you’re searching your library, you may cast Panglacial Wurm from your library.", 9,5, 10.00);

        var carta3 = new Card(3,"Boreal Griffin","Snow Creature — Griffin", "Boreal Griffin gains first strike until end of turn.", 3,2, 0.40);

        var carta31 = new Card(3,"Ronom Unicorn","Creature — Unicorn", "Sacrifice Ronom Unicorn: Destroy target enchantment.", 2,2, 0.45);

        var carta4 = new Card(4,"Squall Drifter","Snow Creature — Elemental", "Flying", 1,1, 1.20);

        var collection1 = new Collection(1, "Dark", LocalDate.now(), new ArrayList<>(List.of(carta2)));

        var collection11 = new Collection(1, "Black", LocalDate.now(), new ArrayList<>(List.of(carta4)));


        var collecao2 = new Collection(2,"Light", LocalDate.now(), new ArrayList<>(List.of(carta3, carta)));

        coleRepo.create(collection1);
        coleRepo.create(collecao2);
        //colecao precisa ser criada antes da carta para que exista a chave estrangeira

        cardRepo.create(carta);
        cardRepo.create(carta2);
        cardRepo.create(carta3);
        cardRepo.create(carta4);
        System.out.println("----------------------");
        cardRepo.get(3);
        System.out.println("----------------------");
        cardRepo.update(3, carta31);
        System.out.println("----------------------");
        cardRepo.get(3);
        System.out.println("----------------------");
        cardRepo.get(2);
        System.out.println("----------------------");
        cardRepo.delete(2);
        System.out.println("----------------------");
        cardRepo.getAllByCollection(2);
        System.out.println("----------------------");
        cardRepo.getAll();

        System.out.println("----------------------");
        coleRepo.getAll();
        System.out.println("----------------------");
        coleRepo.get(1);
        System.out.println("----------------------");
        coleRepo.update(1, collection11);
        System.out.println("----------------------");
        coleRepo.get(1);
        System.out.println("----------------------");
        cardRepo.getAll();
        System.out.println("----------------------");
        coleRepo.delete(2);
        System.out.println("----------------------");
        coleRepo.getAll();
        System.out.println("----------------------");
        cardRepo.getAll();








//        var collection = new Collection(1, "Innistrad: Midnight Hunt (MID)", LocalDate.now(), List.of(carta, carta));
//        System.out.println(collection);



//        final HttpServer server = startServer();
//        System.out.println(String.format("Jersey app started with endpoints available at "
//                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
//        System.in.read();
//        server.stop();

    }
}

