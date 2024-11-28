package in.tech_camp.pictweet.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.pictweet.costom_user.CustomUserDetail;
import in.tech_camp.pictweet.entity.UserEntity;
import in.tech_camp.pictweet.repository.UserRepository;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    // private final SessionRepository sessionRepository;
    // // セッションのタイムアウト（ミリ秒）
    // private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30分
    // private final HttpServletRequest request; // HttpServletRequestをインジェクト

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // // セッション情報の保存処理を追加
        // HttpSession session = request.getSession(); // セッションを取得
        // String sessionId = session.getId(); // JSESSIONIDを取得
        // // 新規ユーザーの処理
        // if (session.isNew()) {
        //     // セッションが新しく作成されたときの処理
        //     System.out.println("新しいセッションが作成されました。セッションID: " + sessionId);
        // }else {
        //     System.out.println("作成済みのセッション。セッションID: " + sessionId);
        // }
        // Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + SESSION_TIMEOUT);

        // SessionInfo sessionInfo = new SessionInfo();
        // sessionInfo.setSessionId(sessionId);
        // sessionInfo.setUserId(userEntity.getId());
        // sessionInfo.setExpirationDate(expirationDate);

        // sessionRepository.insert(sessionInfo); // リポジトリを通してセッションを保存

        return new CustomUserDetail(userEntity);
    }
}
