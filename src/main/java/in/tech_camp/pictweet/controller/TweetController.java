package in.tech_camp.pictweet.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.pictweet.costom_user.CustomUserDetail;
import in.tech_camp.pictweet.entity.TweetEntity;
import in.tech_camp.pictweet.entity.UserEntity;
import in.tech_camp.pictweet.form.SearchForm;
import in.tech_camp.pictweet.form.TweetForm;
import in.tech_camp.pictweet.repository.TweetRepository;
import in.tech_camp.pictweet.repository.UserRepository;
import in.tech_camp.pictweet.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tweets")
@AllArgsConstructor
public class TweetController {
  private final TweetRepository tweetRepository;

  private final UserRepository userRepository;

  @GetMapping("/")
  public List<TweetEntity> showIndex(Model model) {
        List<TweetEntity> tweets = tweetRepository.findAll();
        // SearchForm searchForm = new SearchForm();
        // model.addAttribute("tweets", tweets);
        // model.addAttribute("searchForm", searchForm);
        return tweets;
  }

  @GetMapping("/tweets/new")
  public String showTweetNew(Model model){
    model.addAttribute("tweetForm", new TweetForm());
    return "tweets/new";
  }

  @PostMapping("/tweets")
  public ResponseEntity<?> createTweet(@RequestBody @Validated(ValidationOrder.class) TweetForm tweetForm, BindingResult result, @AuthenticationPrincipal CustomUserDetail currentUser) {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(Collections.singletonMap("errorMessages", errorMessages));
      }

      TweetEntity tweet = new TweetEntity();
      tweet.setUser(userRepository.findById(currentUser.getId()));
      tweet.setText(tweetForm.getText());
      tweet.setImage(tweetForm.getImage());

      try {
        tweetRepository.insert(tweet);

        // ユーザー情報を取得
        UserEntity userDetails = tweet.getUser();

        String jsonResponse = String.format(
          "{\"id\":%d,\"text\":\"%s\",\"image\":\"%s\",\"user\":{\"id\": \"%d\",\"nickname\":\"%s\",\"email\":\"%s\"}}",
          tweet.getId(),
          tweet.getText(),
          tweet.getImage(),
          userDetails.getId(),
          userDetails.getNickname(),
          userDetails.getEmail()
      );
        return ResponseEntity.ok(jsonResponse); // 成功時は保存したツイート情報を返却
        // return ResponseEntity.ok().build(); // 成功時はHTTP 200を返却
      } catch (Exception e) {
          System.out.println("エラー：" + e);
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ツイートの作成に失敗しました");
      }
  }

  @PostMapping("/tweets/{tweetId}/delete")
  public String deleteTweet(@PathVariable("tweetId") Integer tweetId) {
    try {
      tweetRepository.deleteById(tweetId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }

  @GetMapping("/tweets/{tweetId}/edit")
  public String editTweet(@PathVariable("tweetId") Integer tweetId, Model model) {
    TweetEntity tweet = tweetRepository.findById(tweetId);

    TweetForm tweetForm = new TweetForm();
    tweetForm.setText(tweet.getText());
    tweetForm.setImage(tweet.getImage());

    model.addAttribute("tweetForm", tweetForm);
    model.addAttribute("tweetId", tweetId);
    return "tweets/edit";
  }

  @PostMapping("/tweets/{tweetId}/update")
  public String updateTweet(@ModelAttribute("tweetForm") @Validated TweetForm tweetForm,
                            BindingResult result,
                            @PathVariable("tweetId") Integer tweetId,
                            Model model) {

    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);

      model.addAttribute("tweetForm", tweetForm);
      model.addAttribute("tweetId", tweetId);
      return "tweets/edit";
    }

    TweetEntity tweet = tweetRepository.findById(tweetId);
    tweet.setText(tweetForm.getText());
    tweet.setImage(tweetForm.getImage());

    try {
      tweetRepository.update(tweet);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/";
  }

  @GetMapping("/{tweetId}")
  public ResponseEntity<String> showTweetDetail(@PathVariable("tweetId") Integer tweetId) {
       TweetEntity tweet = tweetRepository.findById(tweetId);
      if (tweet == null) {
          return ResponseEntity.notFound().build();
      }
      UserEntity userDetails = tweet.getUser();
        // .を使用するとJSONの仕様に合わずフロント側で値を取得できない
        String jsonResponse = String.format(
          "{\"id\":%d,\"text\":\"%s\",\"image\":\"%s\",\"user\":{\"id\": \"%d\",\"nickname\":\"%s\",\"email\":\"%s\"}}",
          tweet.getId(),
          tweet.getText(),
          tweet.getImage(),
          userDetails.getId(),
          userDetails.getNickname(),
          userDetails.getEmail()
      );
      return ResponseEntity.ok(jsonResponse);
  }

  @GetMapping("/tweets/search")
  public String searchTweets(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
    List<TweetEntity> tweets = tweetRepository.findByTextContaining(searchForm.getText());
    model.addAttribute("tweets", tweets);
    model.addAttribute("searchForm", searchForm);
    return "tweets/search";
  }
}
