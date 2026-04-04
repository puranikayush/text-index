package com.text;

import com.text.api.ITextIndex;
import com.text.api.TextIndexImpl;
import com.text.model.Product;

import java.util.List;
import java.util.Set;

public class Main {
    static void main() {
        ITextIndex index = new TextIndexImpl();
        List<Product> products = getProducts();
        index.buildIndex(products);

        printProductNames("AMUL CHOCOLATE", index.queryByKeywordAll("AMUL CHOCOLATE"));
        printProductNames("AMUL CHOCOLATE", index.queryByKeywordAny("AMUL CHOCOLATE"));

    }

    private static List<Product> getProducts() {
        return List.of(
                new Product(1, "Amul Milk"),
                new Product(2, "Chocolate Milk")
        );
    }

    private static void printProductNames(String query, Set<Product> products) {
        System.out.println("query : " + query);
        products.forEach(product -> System.out.println(product.getName()));
        System.out.println(" -- ");
    }
}
