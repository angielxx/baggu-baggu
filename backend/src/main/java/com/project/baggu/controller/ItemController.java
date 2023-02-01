package com.project.baggu.controller;

import com.project.baggu.dto.BaseIsSuccessDto;
import com.project.baggu.dto.BaseResponseStatus;
import com.project.baggu.dto.ItemDetailDto;
import com.project.baggu.dto.ItemOrderByNeighborDto;
import com.project.baggu.dto.ItemListDto;
import com.project.baggu.dto.TradeRequestDto;
import com.project.baggu.dto.UpdateItemDto;
import com.project.baggu.dto.UpdatedItemDto;
import com.project.baggu.dto.UserItemDto;
import com.project.baggu.dto.UserRegistItemDto;
import com.project.baggu.exception.BaseException;
import com.project.baggu.service.ItemService;
import com.project.baggu.service.TradeRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/baggu/item")
@Slf4j
public class ItemController {

  private final ItemService itemService;
  private final TradeRequestService tradeRequestService;




  //GET baggu/item/{itemIdx}
  //아이템의 상세 정보(아이템 정보, 신청자 정보)를 받는다.
  @GetMapping("/{itemIdx}")
  public ItemDetailDto itemDetail (@PathVariable("itemIdx") Long itemIdx){

    return  itemService.itemDetail(itemIdx);
  }

  //POST baggu/item
  //새로운 아이템을 작성한다.
  @PostMapping
  public BaseIsSuccessDto registItem(@ModelAttribute UserRegistItemDto u) throws Exception {

    Long authUserIdx = Long.parseLong(
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    if (authUserIdx != u.getUserIdx()) {
      throw new BaseException(BaseResponseStatus.UNVALID_USER);
    }

    itemService.registItem(u);

    return new BaseIsSuccessDto(true);
  }

  //PUT /baggu/item/{itemIdx}
  //게시글 정보를 갱신한다.
  @PutMapping("/{itemIdx}")
  public UpdateItemDto updateItem(@PathVariable("itemIdx") Long itemIdx, @RequestBody UpdatedItemDto item){




    return itemService.updateItem(itemIdx, item);
  }

  //DELETE /baggu/item/{itemIdx}
  //게시글을 삭제한다.
  @DeleteMapping("/{itemIdx}")
  public void deleteItem(@PathVariable("itemIdx") Long itemIdx){

    itemService.deleteItem(itemIdx);
  }


  //POST baggu/item/{itemIdx}
  //유저가 신청메세지와 함께 바꾸신청을 보낸다.
  @PostMapping("/{itemIdx}")
  public void tradeRequest(@PathVariable("itemIdx") Long itemIdx, @RequestBody TradeRequestDto tradeRequestDto){

    itemService.tradeRequest(itemIdx, tradeRequestDto);
  }


  //GET baggu/item?dong={dong}&userIdx={userIdx}&keyword={keyword}
  //유저의 동네에 최근 등록된 물품 리스트를 받는다.
  //유저가 등록한 아이템 리스트를 받는다.
  //유저가 입력한 검색어를 기반으로 아이템 리스트를 받는다. pathvariable로 하면 한글 안넘어와
  @GetMapping()
  public List<?> getItemList(@RequestParam(name = "dong", required = false) String dong,
      @RequestParam(name="userIdx", required=false) Long userIdx,
      @RequestParam(name="keyword", required=false) String keyword){

    if(dong!=null){
      return itemService.itemListOrderByNeighbor(dong);
    } else if(userIdx!=null){
      return itemService.getUserItemList(userIdx);
    } else if(keyword!=null){
      return itemService.itemListByItemName(keyword);
    } else{
      throw new BaseException(BaseResponseStatus.UNVALID_PARAMETER);
    }
  }

//
//  @GetMapping(params = {"userIdx"})
//  public List<UserItemDto> userItemList (@RequestParam(name = "userIdx") Long userIdx){
//
//
//  }
//
//  @GetMapping(params = {"keyword"})
//  public List<ItemListDto> itemListByItemName(@RequestParam(name = "keyword") String keyword){
//
//
//  }
//
//
//



}
