package com.greglturnquist.hackingspringboot.reactive.controller;

import com.greglturnquist.hackingspringboot.reactive.dto.Cart;
import com.greglturnquist.hackingspringboot.reactive.repository.CartRepository;
import com.greglturnquist.hackingspringboot.reactive.repository.ItemRepository;
import com.greglturnquist.hackingspringboot.reactive.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {
    private InventoryService inventoryService;
    private ItemRepository itemRepository;
    private CartRepository cartRepository;

    public HomeController(InventoryService inventoryService, ItemRepository itemRepository, CartRepository cartRepository) {
        this.inventoryService = inventoryService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items", this.itemRepository.findAll())
                .modelAttribute("cart", this.cartRepository.findById("My Cart").defaultIfEmpty(new Cart("My Cart")))
                .build());
    }

    @PostMapping("add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.inventoryService.addItemToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String description,
                           @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items", inventoryService.searchByExample(name, description, useAnd))
                .modelAttribute("cart", this.cartRepository.findById("My Cart").defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
}
