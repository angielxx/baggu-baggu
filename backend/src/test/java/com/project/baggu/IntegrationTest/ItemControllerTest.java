package com.project.baggu.IntegrationTest;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.baggu.Security.WithMockCustomTokenAccount;
import com.project.baggu.domain.Item;
import com.project.baggu.domain.ItemImage;
import com.project.baggu.domain.User;
import com.project.baggu.domain.enumType.CategoryType;
import com.project.baggu.dto.TradeRequestDto;
import com.project.baggu.dto.UpdateItemDto;
import com.project.baggu.dto.UserRegistItemDto;
import com.project.baggu.repository.ItemRepository;
import com.project.baggu.repository.UserRepository;
import com.project.baggu.service.ItemService;
import com.project.baggu.service.UserService;
import com.zaxxer.hikari.HikariDataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTest {

  @Autowired
  private UserRepository userRepository;

  @Mock
  private Item item;
  @Mock
  private User user;
  @Mock
  private UserRegistItemDto userRegistItemDto;
  @Mock
  private ItemImage itemImage;
  @Mock
  private TradeRequestDto tradeRequestDto;
  @Mock
  private UpdateItemDto updateItemDto;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ItemRepository itemRepository;

  @Autowired
  UserService userService;

  int initSize = 0;





  @Test
  @DisplayName("?????? ????????? ????????? ????????? ????????? ?????? ???????????? (dong)")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(1)
  public void itemList () throws Exception{

    MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
    info.add("dong", "?????????");
    mockMvc.perform(get("/baggu/item").params(info)).andDo(print()).andExpect(status().isOk());
  }

  @Test
  @DisplayName("1. ????????? ??????")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(2)
  public void saveItem() throws Exception {

    mockMvc.perform(post("/baggu/item")
                .param("userIdx", "1")
                .param("category", "TYPE0")
                .param("itemFirstImgIdx", "0")
                .param("content", "????????????")
                .param("title", "?????????")
            //  .header("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoiVFlQRTUiLCJpYXQiOjE2NzU3NDA0MjgsImV4cCI6MTY3ODMzMjQyOH0.R5SW4a-MDv4CwHM1EBWJ1IfYi97cl6pzlqDsFjjcmSI")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }
  @Test
  @DisplayName("????????? ?????? ?????????")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(3)
  public void itemDetail () throws Exception{

    mockMvc.perform(get("/baggu/item/1")
        ) .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();
  }

  @Test
  @DisplayName("???????????? ??????")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(4)
  public void tradeRequest() throws Exception{

    mockMvc.perform(post("/baggu/item/1")
            //.header("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoiVFlQRTUiLCJpYXQiOjE2NzU3NDA0MjgsImV4cCI6MTY3ODMzMjQyOH0.R5SW4a-MDv4CwHM1EBWJ1IfYi97cl6pzlqDsFjjcmSI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tradeRequestDto))
        ) .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("????????? ????????????")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(5)
  public void update() throws Exception{

    mockMvc.perform(put("/baggu/item/1")
            //.header("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoiVFlQRTUiLCJpYXQiOjE2NzU3NDA0MjgsImV4cCI6MTY3ODMzMjQyOH0.R5SW4a-MDv4CwHM1EBWJ1IfYi97cl6pzlqDsFjjcmSI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateItemDto))
        ) .andDo(print())
        .andExpect(status().isOk());

    Assertions.assertThat("??????").isEqualTo(itemRepository.findById(1L).get().getTitle());
  }

  @Test
  @DisplayName("????????? ??????")
  @WithMockCustomTokenAccount(userIdx = "1")
  @Order(6)
  public void deleteItem() throws Exception{


    mockMvc.perform(delete("/baggu/item/1"))
        .andDo(print())
        .andExpect(status().isOk());

    log.info("item is valid {}", itemRepository.findById(1L).get().isValid());
    Assertions.assertThat(false).isEqualTo(itemRepository.findById(1L).get().isValid());
  }

  @BeforeEach
  @DisplayName("BeforeEach")
  public void beforeEach() {

    user = User.builder().userIdx(1L).build();

    item = Item.builder()
        .title("?????????").content("????????????").itemImages(new ArrayList<>()).itemKeeps(new ArrayList<>())
        .dong("?????????").firstImg("img").category(CategoryType.TYPE1).si("???")
        .dong("???").gu("???").itemIdx(1L).build();

    userRegistItemDto = UserRegistItemDto.builder()
        .content("??????").title("?????????").userIdx(1L).category(CategoryType.TYPE1).itemImgs(new ArrayList<>()).build();

    ArrayList <Long> list = new ArrayList();
    list.add(2L);
    list.add(3L);
    tradeRequestDto = TradeRequestDto.builder()
        .comment("??????").requestItemIdxList(list)
        .requestUserIdx(2L).build();

    ArrayList <String> list2 = new ArrayList<>();
    updateItemDto = UpdateItemDto.builder()
        .category(CategoryType.TYPE1)
        .content("??????")
        .itemFirstImgIdx(0)
        .title("??????")
        .uploadImgUrls(list2)
        .build();

  }


  @AfterEach
  @DisplayName("????????? ??????")
  public void afterEach() {
    log.info("????????? ??????");
  }
}