package ch.openapi.api.model;

import java.util.List;

public record ProductSelection(
        String accountType,
        String productPackage,
        List<String> additionalProducts,
        SelectedProducts selectedProducts,
        String status
) {}
