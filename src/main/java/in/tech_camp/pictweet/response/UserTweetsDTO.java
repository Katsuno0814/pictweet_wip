package in.tech_camp.pictweet.response;

import java.util.List;

import in.tech_camp.pictweet.entity.TweetEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTweetsDTO {
  private  UserDTO user;
  private  List<TweetEntity> tweets;
}