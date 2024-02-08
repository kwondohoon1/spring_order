package com.encore.ordering.order.domain;

import com.encore.ordering.member.domain.Member;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Ordering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)  //디폴틀 값 : name = "member_id"
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Builder
    public Ordering(Member member){
        this.member = member;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCELED;
    }
}
