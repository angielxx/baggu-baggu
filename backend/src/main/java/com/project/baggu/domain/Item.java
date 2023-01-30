package com.project.baggu.domain;
import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.baggu.domain.enumType.CategoryType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_idx")
  private Long itemIdx;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "state")
  private int state;

  @Column(name = "trade_item_idx")
  private Long tradeItemIdx;

  @Column(name = "view_count")
  private int viewCount;

  @Column(name = "is_valid")
  private boolean isValid = true;

  @Column(name = "category")
  @Enumerated(EnumType.ORDINAL)
  private CategoryType category;

  @Column(name = "user_request_count")
  private int userRequestCount;

  @Column(name = "si")
  private String si;

  @Column(name = "gu")
  private String gu;

  @Column(name = "dong")
  private String dong;

  @LastModifiedDate
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "modified_at")
  private LocalDateTime modifiedAt;

  @JsonIgnore
  @OneToOne(mappedBy = "item")
  private ReviewText reviewText;

  @JsonIgnore
  @OneToMany(mappedBy = "item")
  private List<ItemKeep> itemKeeps = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "receiveItemIdx")
  private List<TradeRequest> tradeRequests = new ArrayList<>();

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user")
  private User user;

  public void setUser(User user) {
    this.user = user;
    user.getItems().add(this);
  }


}
