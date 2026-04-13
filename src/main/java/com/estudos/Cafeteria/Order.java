package com.estudos.Cafeteria;

import java.math.BigDecimal;

public record Order(Long id, String description, BigDecimal value, String status) {
}
