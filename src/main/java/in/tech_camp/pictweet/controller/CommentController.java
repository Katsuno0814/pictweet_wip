package in.tech_camp.pictweet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.pictweet.costom_user.CustomUserDetail;
import in.tech_camp.pictweet.entity.CommentEntity;
import in.tech_camp.pictweet.entity.TweetEntity;
import in.tech_camp.pictweet.form.CommentForm;
import in.tech_camp.pictweet.repository.CommentRepository;
import in.tech_camp.pictweet.repository.TweetRepository;
import in.tech_camp.pictweet.repository.UserRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tweets")
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  private final TweetRepository tweetRepository;

  @PostMapping("/{tweetId}/comment")
  public ResponseEntity<CommentEntity> createComment(@PathVariable("tweetId") Integer tweetId,
                            @RequestBody CommentForm commentForm,
                            @AuthenticationPrincipal CustomUserDetail currentUser) {

    TweetEntity tweet = tweetRepository.findTweetById(tweetId);

    CommentEntity comment = new CommentEntity();
    comment.setText(commentForm.getText());
    comment.setTweet(tweet);
    comment.setUser(userRepository.findUserById(currentUser.getId()));

    try {
      commentRepository.insert(comment);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
    }

    return ResponseEntity.ok(comment);

  }
}
