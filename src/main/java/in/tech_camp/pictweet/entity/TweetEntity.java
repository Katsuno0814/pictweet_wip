package in.tech_camp.pictweet.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TweetEntity {
  private Integer id;
  private String name;
  private String text;
  private String image;
  private Timestamp createdAt;
  private Integer userId;
}
