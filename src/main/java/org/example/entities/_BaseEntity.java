package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public abstract class _BaseEntity {
    private int id;

    public _BaseEntity(int id) {
        this.id = id;
    }

    public _BaseEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", _BaseEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (id < 0)
            errors.add("O id não pode ser menor que zero");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
