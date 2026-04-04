package com.text.api;

import com.text.model.Product;

import java.util.*;

public class TextIndexImpl implements ITextIndex {
    private static final String DELIMITER = "\\s+";
    private final Map<String, Set<Product>> tokenProductMap;

    public TextIndexImpl() {
        this.tokenProductMap = new HashMap<>();
    }

    @Override
    public void buildIndex(List<Product> products) {
        for (Product product : products) {
            String name = product.getName();
            String nName = normalizeString(name);
            String[] tokens = nName.split(DELIMITER);
            for (String token : tokens) {
                tokenProductMap.computeIfAbsent(token, (k) -> new HashSet<Product>()).add(product);
            }
        }
    }

    private String normalizeString(String name) {
        return name.toLowerCase();
    }

    @Override
    public List<Product> queryByKeywordAll(String query) {
        List<Product> result = new ArrayList<>();
        String nQuery = normalizeString(query);
        String[] qTokens = nQuery.split(DELIMITER);
        boolean first = true;
        for (String qToken : qTokens) {
            if (tokenProductMap.containsKey(qToken)) {
                Set<Product> products = tokenProductMap.get(qToken);
                if (first) {
                    first = false;
                    result.addAll(products);
                } else {
                    result.retainAll(products);
                }
                if (result.isEmpty()) return Collections.emptyList();
            } else {
                return Collections.emptyList();
            }
        }
        return result;
    }

    @Override
    public List<Product> queryByKeywordAny(String query) {
        List<Product> result = new ArrayList<>();
        String nQuery = normalizeString(query);
        String[] qTokens = nQuery.split(DELIMITER);
        for (String qToken : qTokens) {
            Set<Product> products = tokenProductMap.get(qToken);
            result.addAll(products);
        }
        return result;
    }
}
