package in.tech_camp.pictweet.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
  private Integer id;
  private String nickname;
}
