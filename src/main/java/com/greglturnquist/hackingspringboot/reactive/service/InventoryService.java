package com.greglturnquist.hackingspringboot.reactive.service;

import com.greglturnquist.hackingspringboot.reactive.dto.Item;
import com.greglturnquist.hackingspringboot.reactive.repository.ItemRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class InventoryService {
    private ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);
        return itemRepository.findAll(probe);
    }
}
