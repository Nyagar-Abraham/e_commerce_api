package org.abraham.e_commerce_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "street_no")
    private String streetNo;

    @Column(name = "building_name")
    private String buildingName;


    @Column(name = "locality")
    private String locality;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User customer;

    public void addUser(User user) {
        customer = user;
        user.getAddresses().add(this);
    }

//    @OneToMany(mappedBy = "address")
//    private Set<Order> orders = new LinkedHashSet<>();
}