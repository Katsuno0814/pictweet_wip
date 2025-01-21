package in.tech_camp.pictweet.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetControllerTest {
    @Test
    public void testCreateTweet() {
        String tweetJson = """
            {
                "text": "This is a test tweet",
                "image": null
            }
        """;

        given()
            .contentType("application/json")
            .body(tweetJson)
            .when()
            .post("/tweets")
            .then()
            .statusCode(302); // リダイレクトされることを確認
    }

    @Test
    public void testDeleteTweet() {
        int tweetId = 28; // 存在するツイートIDを指定

        given()
        .pathParam("tweetId", tweetId)
        .when()
        .post("/tweets/{tweetId}/delete")
        .then()
        .statusCode(302); // リダイレクトされることを確認
    }
    @Test
    public void testShowTweetDetail() {
      given()
          .when()
          .get("/tweets/28") // tweetId=1 のツイートを取得
          .then()
          .statusCode(200); // ステータスコードが200であることを確認
    }
}