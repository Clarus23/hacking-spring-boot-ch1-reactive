package com.greglturnquist.hackingspringboot.reactive;

import com.greglturnquist.hackingspringboot.reactive.dto.Cart;
import com.greglturnquist.hackingspringboot.reactive.dto.CartItem;
import com.greglturnquist.hackingspringboot.reactive.dto.Item;
import com.greglturnquist.hackingspringboot.reactive.repository.CartRepository;
import com.greglturnquist.hackingspringboot.reactive.repository.ItemRepository;
import com.greglturnquist.hackingspringboot.reactive.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class InventoryServiceUnitTest {
    InventoryService inventoryService;                  // CUT(class unter test) : 테스트 대상 클래스
    @MockBean private ItemRepository itemRepository;    // 협력자 클래스(목 객체)
    @MockBean private CartRepository cartRepository;    // 협력자 클래스(목 객체)

    @BeforeEach
    void setUp() {
        // 테스트 데이터 정의
        Item sampleItem = new Item("item1", "TV tray", "Alf TV Tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // 협력자와의 상호작용 정의
        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() {
        inventoryService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create)
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItems()).extracting(CartItem::getQuantity)
                            .containsExactlyInAnyOrder(1);
                    assertThat(cart.getCartItems()).extracting(CartItem::getItem)
                            .containsExactly(new Item("item1", "TV tray", "Alf TV Tray", 19.99));

                    return true;
                })
                .verifyComplete();
    }

}
