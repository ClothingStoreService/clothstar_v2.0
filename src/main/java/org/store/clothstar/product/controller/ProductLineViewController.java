package org.store.clothstar.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductLineViewController {
    @GetMapping("/productLinePagingOffset")
    public String productLinePagingOffset() {
        return "productLineOffsetList";
    }

    @GetMapping("/productLinePagingSlice")
    public String productLinePagingSlice() {
        return "productLineSliceList";
    }
}
