package com.project.baggu.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {

  private MultipartFile profileImg;
  private String nickname;
  private String info;
  private ArrayList<ItemListDto> itemList = new ArrayList<>();

}
