package com.project.baggu.service;

import com.project.baggu.domain.enumType.Role;
import com.project.baggu.exception.BaseException;
import com.project.baggu.exception.BaseResponseStatus;
import com.project.baggu.domain.Category;
import com.project.baggu.domain.Item;
import com.project.baggu.domain.Notify;
import com.project.baggu.domain.User;
import com.project.baggu.dto.*;
import com.project.baggu.repository.CategoryRepository;
import com.project.baggu.repository.ItemRepository;
import com.project.baggu.repository.NotifyRepository;
import com.project.baggu.repository.ReviewTagRepository;
import com.project.baggu.repository.ReviewTextRepository;
import com.project.baggu.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final NotifyRepository notifyRepository;
  private final ReviewTextRepository reviewTextRepository;
  private final ReviewTagRepository reviewTagRepository;
  private final S3UploadService s3UploadService;
  private final String IMAGE_DIR_ITEM = "item";

  private final String IMAGE_DIR_USER = "user";

  @Transactional
  public UserProfileDto userSignUp(UserSignUpDto userSignUpDto) throws BaseException {

    User user = userRepository.findUserByKakaoId(userSignUpDto.getKakaoId())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.OAUTH_REQUIRE));

    user.setEmail(userSignUpDto.getEmail());
    user.setNickname(userSignUpDto.getNickname());
    user.setCategories(new ArrayList<>());
    user.setSi(userSignUpDto.getSi());
    user.setGu(userSignUpDto.getGu());
    user.setDong(userSignUpDto.getDong());
    user.setLng(userSignUpDto.getLng());
    user.setLat(userSignUpDto.getLat());
    user.setRole(Role.afterSignUp(user.getRole()));

    userRepository.save(user);

    userSignUpDto.getCategory()
        .stream()
        .forEach((c) -> categoryRepository.save(Category.builder().user(user).type(c).build()));

    return UserProfileDto.builder()
        .userIdx(user.getUserIdx())
        .nickname(user.getNickname())
        .info(user.getInfo())
        .dong(user.getDong())
        .role(user.getRole()).build();
  }

  public UserProfileDto getUserProfile(Long userIdx) throws BaseException {

    User user = userRepository.findById(userIdx)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.DATABASE_GET_ERROR));

    UserProfileDto userProfileDto =  UserProfileDto.builder()
        .userIdx(user.getUserIdx())
        .nickname(user.getNickname())
        .info(user.getInfo())
        .dong(user.getDong())
        .role(user.getRole())
        .profileImg(user.getProfileImg())
        .build();

    return userProfileDto;
  }

  @Transactional
  public void updateUserLocation(Long userIdx, UserUpdateLocationDto userUpdateLocationDto) {

    User user = userRepository.findById(userIdx)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.DATABASE_GET_ERROR));

    user.setSi(userUpdateLocationDto.getSi());
    user.setGu(userUpdateLocationDto.getGu());
    user.setDong(userUpdateLocationDto.getDong());
    user.setLat(userUpdateLocationDto.getLat());
    user.setLng(userUpdateLocationDto.getLng());

    userRepository.save(user);
  }

  public UserDetailDto getUserDetail(Long userIdx) throws BaseException {

    User user = userRepository.findById(userIdx)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.DATABASE_GET_ERROR));

    UserDetailDto userDetailDto = UserDetailDto.builder()
        .info(user.getInfo())
        .nickname(user.getNickname())
        .profileImg(user.getProfileImg())
        .build();

    itemRepository.getUserItemList(userIdx).stream().forEach(
        (item) ->
            userDetailDto.getItemList().add(
                ItemListDto.builder()
                    .title(item.getTitle())
                    .dong(item.getDong())
                    .createdAt(item.getCreatedAt())
                    .state(item.getState())
                    .isValid(item.isValid())
                    .build()
            )
    );

    return userDetailDto;
  }

  public ReviewDto getReviewInfo(Long userIdx) {

    ReviewDto reviewDto = new ReviewDto();

    //받은 태그 리뷰
    reviewTagRepository.findReviewTagByUserIdx(userIdx).stream().forEach(
        (rt)->
            reviewDto.getReviewTag().put(rt.getType().ordinal(),
                reviewDto.getReviewTag().getOrDefault(rt.getType().ordinal(), 0) + 1)
    );

    //보낸 텍스트 리뷰
    reviewTextRepository.findReviewReceiveTextListByUserIdx(userIdx).stream().forEach(
        (c) ->
            reviewDto.getReceiveReviewText().add(c)
    );

    //받은 텍스트 리뷰(정보)
    reviewTextRepository.findReviewRequestTextListByUserIdx(userIdx).stream().forEach(
        (rt) ->
            reviewDto.getRequestReviewText().add(
                ReviewTextDto.builder()
                    .targetItemIdx(rt.getItem().getItemIdx())
                    .reviewText(rt.getComment())
                    .writeUserIdx(rt.getUser().getUserIdx())
                    .build()
            )
    );

    return reviewDto;
  }

  @Transactional
  public void updateUserProfile(Long userIdx, UserUpdateProfileDto userUpdateProfileDto)
      throws Exception {

    User user = userRepository.findById(userIdx).orElseThrow(()->new BaseException(BaseResponseStatus.DATABASE_GET_ERROR));

    //만약 이미지가 존재하면, 이미지 업로드 처리
    if(userUpdateProfileDto.getProfileImg()!=null){
      String uploadUrl = s3UploadService.upload(userUpdateProfileDto.getProfileImg(), IMAGE_DIR_USER);
      user.setProfileImg(uploadUrl);
    }

    //그 외 정보 처리
    user.setInfo(userUpdateProfileDto.getInfo());
    user.setNickname(userUpdateProfileDto.getNickname());

    userRepository.save(user);
  }


  //============================
  //이하 2순위 이하 API (미완성)
  //============================


  @Transactional

  public User findUser(Long userIdx) {

    return userRepository.findById(userIdx).get();
  }

  public boolean findUserByEmail(String email) {

    Optional<User> user = userRepository.findUserByEmail(email);
    if (user.isPresent()) {
      return true;
    } else {
      return false;
    }

  }


  public List<NotifyDto> notifyList(Long userIdx) {

    List<Notify> notifyList = notifyRepository.notifyListByUserIdx(userIdx);
    List<NotifyDto> notifyDtoList = new ArrayList<>();

    for (Notify n : notifyList) {
      NotifyDto notifyDto = new NotifyDto();
      notifyDto.setNotifyType(n.getType().ordinal());
      notifyDto.setNotifyIdx(n.getNotifyIdx());
      notifyDto.setTitle(n.getTitle());
      notifyDto.setContent(n.getContent());
      notifyDtoList.add(notifyDto);
    }
    return notifyDtoList;
  }



  //tradeCount 0 으로 초기화
  @Transactional
  public void raceConditionUpdateUser(Long userIdx) {
    User user = userRepository.findById(userIdx).get();
    user.setTradeCount(0);
  }


  @Transactional
  public void raceConditionTest(Long userIdx) {
    Optional<User> user = userRepository.findById(userIdx);
    if (user.isPresent()) {
      log.info("여기");
      User u = user.get();
      u.setTradeCount(u.getTradeCount() + 1);
      log.info("count -> {}", u.getTradeCount());
    }
  }

  public int raceConditionTradeCountUser(Long userIdx) {
    User user = userRepository.findById(userIdx).get();
    log.info("사용자 정보");
    log.info("{} {} {} {}", user.getUserIdx(), user.getInfo(), user.getName(),
        user.getTradeCount());
    log.info("{} 트레이드 카운트", userRepository.findById(userIdx).get().getTradeCount());
    return userRepository.findById(userIdx).get().getTradeCount();
  }


  public List<ItemListDto> userKeepItemList(Long userIdx) {

    List<Item> list = itemRepository.userKeepItemList(userIdx);
    ItemListDto itemListDto = new ItemListDto();
    List<ItemListDto> itemListDtos = new ArrayList<>();
    for (Item i : list) {
      itemListDto.setTitle(i.getTitle());
      itemListDto.setDong(i.getDong());
      itemListDto.setState(i.getState());
      itemListDto.setCreatedAt(i.getCreatedAt());
      itemListDtos.add(itemListDto);
    }
    return itemListDtos;
  }


  //내부 로직 수행에서 필요 -> auth
  public void save(User user) {
    userRepository.save(user);
  }

  public Optional<User> findUserByKakaoId(String kakaoId) {
    return userRepository.findUserByKakaoId(kakaoId);
  }
}
