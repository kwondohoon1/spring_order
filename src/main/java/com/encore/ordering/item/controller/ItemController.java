package com.encore.ordering.item.controller;

import com.encore.ordering.common.CommonResponseDto;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.dto.ItemResDto;
import com.encore.ordering.item.dto.ItemSearchDto;
import com.encore.ordering.item.repository.ItemRepository;
import com.encore.ordering.item.service.ItemService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    public ItemController(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/item/create")
    public ResponseEntity<CommonResponseDto> itemCreate(ItemReqDto itemReqDto){
        Item item =itemService.create(itemReqDto);
        return new ResponseEntity<>(new CommonResponseDto
                (HttpStatus.CREATED, "item successfully create", item.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResDto>> items(ItemSearchDto itemSearchDto, Pageable pageable){
        List<ItemResDto> itemResDtos = itemService.findAll(itemSearchDto, pageable);
        return new ResponseEntity<>(itemResDtos, HttpStatus.OK);
    }

    @GetMapping("/items/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id){
        Resource resource = itemService.getImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/item/{id}/update")
    public ResponseEntity<CommonResponseDto> itemUpdate(@PathVariable Long id, ItemReqDto itemReqDto){
        Item item = itemService.update(id, itemReqDto);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.OK ,
                "item successfully updated", item.getId()),HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponseDto> itemDelete(@PathVariable Long id){
        Item item = itemService.delete(id);
        return new ResponseEntity<>(new CommonResponseDto
                (HttpStatus.OK, "item successfully deleted", item.getId()), HttpStatus.OK);
    }





}
