package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.example.entities.Collection;
import org.example.repositories.CollectionRepository;
import org.example.services.CollectionService;

import java.util.List;

@Path("colecoes")
public class CollectionResource {
    public CollectionRepository collectionRepository;
    public CollectionService collectionService;

    public CollectionResource(){
        collectionRepository = new CollectionRepository();
        collectionService = new CollectionService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Collection> getAllFiltro(
            @QueryParam("orderby") String orderBy,
            @QueryParam("direction") String direction,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ){
        return collectionRepository.getAllFiltro(orderBy,direction, limit, offset);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        var colecao = collectionRepository.get(id);
        return colecao.isPresent() ?
                Response.ok(colecao.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Collection colecao){
        try{
            collectionService.create(colecao);
            return Response.status(Response.Status.CREATED).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Collection colecao){
        try{
            collectionService.update(id, colecao);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") int id){

    }
}
