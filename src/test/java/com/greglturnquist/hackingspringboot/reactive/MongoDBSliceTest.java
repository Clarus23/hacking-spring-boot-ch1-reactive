package com.greglturnquist.hackingspringboot.reactive;

import com.greglturnquist.hackingspringboot.reactive.dto.Item;
import com.greglturnquist.hackingspringboot.reactive.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class MongoDBSliceTest {
    @Autowired ItemRepository itemRepository;

    @Test
    void itemRepositoryTest() {
        Item sampleItem = new Item("name", "description", 1.99);

        itemRepository.save(sampleItem)
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertThat(item.getId()).isNotNull();
                    assertThat(item.getName()).isEqualTo("name");
                    assertThat(item.getDescription()).isEqualTo("description");
                    assertThat(item.getPrice()).isEqualTo(1.99);

                    return true;
                })
                .verifyComplete();
    }
}
