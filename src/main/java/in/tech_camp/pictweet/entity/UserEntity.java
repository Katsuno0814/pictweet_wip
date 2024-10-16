package in.tech_camp.pictweet.entity;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String nickname;
  private String email;
  private String password;
}
