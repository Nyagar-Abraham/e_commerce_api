package org.abraham.e_commerce_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "cart_total")
    private BigDecimal cartTotal;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User customer;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE,fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();

    public void addCartItem(CartItem cartItem){
        cartItem.setCart(this);
        items.add(cartItem);
    }
    public BigDecimal getCartTotal() {
        return items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}