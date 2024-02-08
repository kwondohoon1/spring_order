package com.encore.ordering.order.controller;


import com.encore.ordering.common.CommonResponseDto;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponseDto> orderCreate(@RequestBody OrderReqDto orderReqDto){
        Ordering ordering = orderService.create(orderReqDto);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.OK, "order success", null),HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponseDto> orderCancel(@PathVariable Long id){
        System.out.println(id);
        Ordering ordering= orderService.cancel(id);
        return new ResponseEntity<>(new CommonResponseDto
                (HttpStatus.OK, "cancel ok", ordering.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<OrderResDto> orderList(){
        return orderService.findAll();
    }

    }


