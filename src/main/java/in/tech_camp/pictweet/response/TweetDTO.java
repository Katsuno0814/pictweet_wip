package in.tech_camp.pictweet.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TweetDTO {
  private Integer id;
  private String text;
  private String image;
  private  UserDTO user;
}
