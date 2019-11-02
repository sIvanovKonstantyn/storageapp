package com.home.kivanov.examples.goods;

import java.io.Serializable;
import java.util.Objects;

public class Goods implements Serializable {

    private String name;
    private String description;

    public Goods(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Goods(String dataString) {
        final String[] data = dataString.split(";");
        this.name = data[0];
        this.description = data[1];
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return Objects.equals(name, goods.name) &&
                Objects.equals(description, goods.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
