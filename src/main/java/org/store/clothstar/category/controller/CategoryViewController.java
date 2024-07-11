package org.store.clothstar.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryViewController {

    @GetMapping("/categoryPagingOffset")
    public String categoryPagingOffset() {
        return "category-product_lines-offset";
    }

    @GetMapping("/categoryPagingSlice")
    public String categoryPagingSlice() {
        return "category-product_lines-slice";
    }
}
