package com.text.api;

import com.text.model.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TextIndexImpl implements ITextIndex {
    private static final String DELIMITER = "\\s+";
    private volatile Map<String, Set<Product>> tokenProductMap;

    public TextIndexImpl() {
        this.tokenProductMap = new ConcurrentHashMap<>();
    }

    @Override
    public void buildIndex(List<Product> products) {
        Map<String, Set<Product>> tokenProductMapTemp = new ConcurrentHashMap<>();
        for (Product product : products) {
            String name = product.getName();
            String nName = normalizeString(name);
            String[] tokens = nName.split(DELIMITER);
            for (String token : tokens) {
                tokenProductMapTemp.computeIfAbsent(token, (k) -> Collections.synchronizedSet(new HashSet<>())).add(product);
            }
        }
        this.tokenProductMap = tokenProductMapTemp;
    }

    private String normalizeString(String name) {
        return name.toLowerCase();
    }

    @Override
    public Set<Product> queryByKeywordAll(String query) {
        Set<Product> result = new HashSet<>();
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
                    if (result.size() < products.size())
                        result.retainAll(products);
                    else {
                        HashSet<Product> temp = new HashSet<>(products);
                        temp.retainAll(result);
                        result = new HashSet<>(temp);
                    }
                }
                if (result.isEmpty()) break;
            } else {
                return Collections.emptySet();
            }
        }
        return result;
    }

    @Override
    public Set<Product> queryByKeywordAny(String query) {
        Set<Product> result = new HashSet<>();
        String nQuery = normalizeString(query);
        String[] qTokens = nQuery.split(DELIMITER);
        for (String qToken : qTokens) {
            Set<Product> products = tokenProductMap.get(qToken);
            result.addAll(products);
        }
        return result;
    }
}
