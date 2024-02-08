package com.encore.ordering.order.dto;

import lombok.Data;

import java.util.List;

//@Data
//public class OrderReqDto {
//    private List<Long> itemIds;
//    private List<Long> counts;
//}

// 예시데이터
/*
{
    "iteamIds" : [1,2], "counts" : [10,20]


 */
@Data
public class OrderReqDto {
    private List<OrderReqItemDto> orderReqItemDtos;

    @Data
    public static class OrderReqItemDto{
        private Long itemId;
        private int count;

    }
}
/*
"orderReqItemDtos" :[
{"itemId" : 1, "count" : 10}
{"itemId" : 2, "count" : 2}

]
 */