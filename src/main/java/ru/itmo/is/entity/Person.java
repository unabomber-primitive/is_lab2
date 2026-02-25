package ru.itmo.is.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Person implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color")
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private Color hairColor;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "person_location_x")),
        @AttributeOverride(name = "y", column = @Column(name = "person_location_y")),
        @AttributeOverride(name = "name", column = @Column(name = "person_location_name"))
    })
    private Location location;

    @Column(nullable = false)
    private double weight;

    @Column(name = "passport_id", nullable = false, unique = true, length = 35)
    private String passportID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country nationality;

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
}
