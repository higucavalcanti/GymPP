package com.project.gymweb.services.discount;

import com.project.gymweb.utils.DiscountStrategy;

public class SemiannualDiscountStrategy implements DiscountStrategy {
    @Override
    public Double apllyDiscount(Double value) {
        return value - (value * 10 / 100);
    }
}
