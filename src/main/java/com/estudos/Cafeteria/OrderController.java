package com.estudos.Cafeteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order Manager")
public class OrderController {
    private static final Map<Long, Order> orderMap = new ConcurrentHashMap<>();
    private static final AtomicLong idOrder = new AtomicLong();


    static {
        long id1 = idOrder.incrementAndGet();
        orderMap.put(id1, new Order(
                id1, "1 Espresso", new BigDecimal("9.50"), "P"
        ));
        long id2 = idOrder.incrementAndGet();
        orderMap.put(id2, new Order(
                id2, "3 Pure chocolate", new BigDecimal("16.90"), "R"
        ));

    }

    @GetMapping
    public Collection<Order> searchAll() {
        return orderMap.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order", description = "Create a new order")
    public Order create(@RequestBody Order orderRequest) {
        long id = idOrder.incrementAndGet();
        Order newOrder = new Order(id, orderRequest.description(), orderRequest.value(), "R");
        orderMap.put(id, newOrder);
        return newOrder;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> searchById(@PathVariable Long id) {
        Order order = orderMap.get(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!orderMap.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        orderMap.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order orderRequest) {
        if (!orderMap.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Order newOrder = new Order(orderRequest.id(), orderRequest.description(), orderRequest.value(), orderRequest.status());
        orderMap.put(orderRequest.id(), newOrder);
        return ResponseEntity.ok(newOrder);
    }
}
