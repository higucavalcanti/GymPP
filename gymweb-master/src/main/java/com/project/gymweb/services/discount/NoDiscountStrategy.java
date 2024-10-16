package com.project.gymweb.services.discount;

import com.project.gymweb.utils.DiscountStrategy;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public Double apllyDiscount(Double value) {
        return value;
    }
}
