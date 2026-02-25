package ru.itmo.is.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Coordinates implements Serializable {
    @Column(nullable = false)
    private float x;

    @Column(nullable = false)
    private Double y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
