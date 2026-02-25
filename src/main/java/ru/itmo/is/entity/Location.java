package ru.itmo.is.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Location implements Serializable {
    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Long y;

    @Column(length = 709)
    private String name;

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
