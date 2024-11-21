package in.tech_camp.pictweet.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.pictweet.entity.TweetEntity;
import in.tech_camp.pictweet.entity.UserEntity;
import in.tech_camp.pictweet.form.UserForm;
import in.tech_camp.pictweet.repository.TweetRepository;
import in.tech_camp.pictweet.repository.UserRepository;
import in.tech_camp.pictweet.response.UserDTO;
import in.tech_camp.pictweet.response.UserTweetsDTO;
import in.tech_camp.pictweet.service.UserService;
import in.tech_camp.pictweet.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

  private final UserRepository userRepository;
  private final TweetRepository tweetRepository;

  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<?> createUser(@RequestBody @Validated(ValidationOrder.class) UserForm userForm, BindingResult result) {
      userForm.validatePasswordConfirmation(result);
      if (userRepository.existsByEmail(userForm.getEmail())) {
          return ResponseEntity.badRequest().body(Map.of("messages", List.of("Email already exists")));
      }

      if (result.hasErrors()) {
          List<String> errorMessages = result.getAllErrors().stream()
                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                  .collect(Collectors.toList());
          return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
      }

      UserEntity userEntity = new UserEntity();
      userEntity.setNickname(userForm.getNickname());
      userEntity.setEmail(userForm.getEmail());
      userEntity.setPassword(userForm.getPassword());

      try {
        userService.createUser(userEntity);
        return ResponseEntity.ok(Map.of(
            "id", userEntity.getId(),
            "nickname", userEntity.getNickname(),
            "email", userEntity.getEmail()
        ));
      } catch (Exception e) {
          return ResponseEntity.internalServerError().body(Map.of("messages", List.of("ユーザー登録に失敗しました")));
      }
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserTweetsDTO> showMypage(@PathVariable("userId") Integer userId) {
    UserEntity user = userRepository.findById(userId);
    UserDTO userDto = new UserDTO(user.getId(), user.getNickname());

    List<TweetEntity> tweets = user.getTweets();

    UserTweetsDTO response = new UserTweetsDTO(userDto, tweets);

    return ResponseEntity.ok(response);
  }
}
