package org.example.entities;

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
}
