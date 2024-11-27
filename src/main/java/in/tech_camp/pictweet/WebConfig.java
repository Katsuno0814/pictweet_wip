package in.tech_camp.pictweet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import in.tech_camp.pictweet.repository.SessionRepository;
import in.tech_camp.pictweet.entity.SessionInfo;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // SessionRepositoryのインジェクション
    @Autowired
    private SessionRepository sessionRepository;

    // セキュリティ設定を利用して、許可されたパスを管理する
    private static final List<String> ALLOWED_PATHS = List.of(
      "api/user",
      "api/login",
      "/api/tweets/",              // ツイート一覧画面
      "/api/tweets/[0-9]+",      // ツイート詳細画面（数字のID）
      "/tweets/search"
  );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new HandlerInterceptor() {
        //   @Override
        //   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //       String requestURI = request.getRequestURI();
        //       String method = request.getMethod();
        //       // セッションIDを取得
        //       String sessionId = request.getRequestedSessionId();

        //       // 許可リストにリクエストURIが含まれているか確認
        //       boolean isAllowed = ALLOWED_PATHS.stream().anyMatch(path -> requestURI.matches(path));

        //       // POSTメソッドや編集・削除エンドポイントは許可しない
        //       if (method.equals("POST") && requestURI.startsWith("/api/tweets/")) {
        //         // セッションIDがnullでなければ、DBでセッションをチェック
        //         if (sessionId != null) {
        //           // DBからセッション情報を取得
        //           SessionInfo sessionInfo = sessionRepository.findBySessionId(sessionId);
        //           if (sessionInfo != null) {
        //               // セッションが存在する場合、リクエストを続行
        //               return true;
        //           }
        //         }
        //           return false; // POSTメソッドを拒否するためにfalseを返す
        //       }

        //       // 編集・削除リクエストを拒否
        //       if (requestURI.matches("/api/tweets/[0-9]+/(edit|delete)")) {
        //         // セッションIDがnullでなければ、DBでセッションをチェック
        //         if (sessionId != null) {
        //           // DBからセッション情報を取得
        //           SessionInfo sessionInfo = sessionRepository.findBySessionId(sessionId);
        //           if (sessionInfo != null) {
        //               // セッションが存在する場合、リクエストを続行
        //               return true;
        //           }
        //         }
        //         return false; // 編集や削除のリクエストを拒否
        //       }

        //       // 許可されている場合はリクエストを続行
        //       if (isAllowed) {
        //           return true;
        //       }

        //       // セッションが無効な場合は、ログイン画面へのリダイレクト
        //       response.sendRedirect("/users/login"); // ログイン画面への遷移
        //       return false; // リクエストを続行しない
        //   }
        // });
    }
}
