package org.store.clothstar.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderViewController {
    @GetMapping("/ordersPagingOffset")
    public String ordersPagingOffset() {
        return "orderOffsetList";
    }

    @GetMapping("/ordersPagingSlice")
    public String ordersPagingSlice() {
        return "orderSliceList";
    }
}
