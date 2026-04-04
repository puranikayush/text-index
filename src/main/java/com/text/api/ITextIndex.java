package com.text.api;

import com.text.model.Product;

import java.util.List;

public interface ITextIndex {

    void buildIndex(List<Product> products);

    //single and multiple
    List<Product> queryByKeywordAll(String keyword);

    //single and multiple
    List<Product> queryByKeywordAny(String keyword);

}
