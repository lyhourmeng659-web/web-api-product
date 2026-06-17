package com.setec.entities;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tbl_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double price;
    private int qty;
    // igore this field when return json response
    @JsonIgnore
    private String imageUrl;

    public double getAmount() {
        return price * qty;
    }

    // get full image url, for example
    // http://localhost:8080/static/uniqueFileName.jpg
    public String getFullImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + imageUrl;
    }

}
