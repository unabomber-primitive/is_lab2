package ru.itmo.is.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    @Column(name = "zip_code", length = 255)
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
