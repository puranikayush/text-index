package com.text.api;

import com.text.model.Product;

import java.util.List;
import java.util.Set;

public interface ITextIndex {

    void buildIndex(List<Product> products);

    //single and multiple
    Set<Product> queryByKeywordAll(String keyword);

    //single and multiple
    Set<Product> queryByKeywordAny(String keyword);

}
