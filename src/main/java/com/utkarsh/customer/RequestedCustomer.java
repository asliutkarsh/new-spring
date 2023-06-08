package com.utkarsh.customer;

public record RequestedCustomer(
        String name,
        String email,
        Integer age
) {
}
