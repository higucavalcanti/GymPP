package com.project.gymweb.services.discount;

import com.project.gymweb.utils.DiscountStrategy;

public class AnnualDiscountStrategy implements DiscountStrategy {
    @Override
    public Double apllyDiscount(Double value) {
        return value - (value * 20 / 100);
    }
}
