package com.encore.ordering.order.dto;

import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResDto {

    private Long id;
    private String memberEmail;
    private String orderStatus;
    private List<OrderResDto.OrderResItemDto> orderItems;

        @Data
        public static class OrderResItemDto{
            private Long id;
            private Long itemId;
            private String itemName;
            private int count;

        }

        public static OrderResDto toDto(Ordering ordering) {
            OrderResDto orderResDto = new OrderResDto();
            orderResDto.setId(ordering.getId());
            orderResDto.setMemberEmail(ordering.getMember().getEmail());
            orderResDto.setOrderStatus(ordering.getOrderStatus().toString());
            List<OrderResDto.OrderResItemDto> orderResItemDtos = new ArrayList<>();
            for(OrderItem orderItem : ordering.getOrderItems()){
                OrderResDto.OrderResItemDto dto = new OrderResDto.OrderResItemDto();
                dto.setId(orderItem.getId());
                dto.setItemId(orderItem.getItem().getId());
                dto.setItemName(orderItem.getItem().getName());
                dto.setCount(orderItem.getQuantity());
                orderResItemDtos.add(dto);
            }
            orderResDto.setOrderItems(orderResItemDtos);
            return orderResDto;
        }
    }