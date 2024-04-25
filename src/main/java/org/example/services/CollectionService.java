package org.example.services;


import org.example.entities.Collection;
import org.example.repositories.CollectionRepository;

public class CollectionService {
    private CollectionRepository collectionRepository;

    public CollectionService(){
        collectionRepository = new CollectionRepository();
    }

    public void create(Collection colecao){
        var validation = colecao.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            collectionRepository.create(colecao);
    }

    public void update(int id, Collection colecao){
        var validation = colecao.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            collectionRepository.update(id, colecao);
    }
    public void delete(int id){
        collectionRepository.delete(id);
    }
}
