package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.Card;
import org.example.repositories.CardRepository;
import org.example.services.CardService;

import java.util.List;


@Path("cartas")
public class CardResource {

    public CardRepository cardRepository;
    public CardService cardService;

    public CardResource(){
        cardRepository = new CardRepository();
        cardService = new CardService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Card> getAll(
            @QueryParam("orderby") String orderBy,
            @QueryParam("direction") String direction,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ){
       return cardRepository.getAll(orderBy, direction, limit, offset);
    }

    @GET
    @Path("colecao/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllByCollection(@PathParam("id") int idCollection){
        return Response.ok(cardRepository.getAllByCollection(idCollection)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        var carta = cardRepository.get(id);
        return carta.isPresent() ?
                Response.ok(carta.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Card carta){
        try{
            cardService.create(carta);
            return Response.status(Response.Status.CREATED).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Card carta){
        try{
            cardService.update(id, carta);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id){
        try{
            cardService.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
