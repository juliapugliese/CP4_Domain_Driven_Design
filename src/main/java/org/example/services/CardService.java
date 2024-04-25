package org.example.services;

import org.example.entities.Card;
import org.example.repositories.CardRepository;

public class CardService {

    private CardRepository cardRepository;

    public CardService(){
        cardRepository = new CardRepository();
    }

    public void create(Card carta){
        var validation = carta.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            cardRepository.create(carta);
    }

    public void update(int id, Card carta){
        var validation = carta.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            cardRepository.update(id, carta);
    }

    public void delete(int id){
        cardRepository.delete(id);
    }
}
