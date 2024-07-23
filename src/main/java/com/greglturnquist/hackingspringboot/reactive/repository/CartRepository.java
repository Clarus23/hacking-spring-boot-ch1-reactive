package com.greglturnquist.hackingspringboot.reactive.repository;

import com.greglturnquist.hackingspringboot.reactive.dto.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
