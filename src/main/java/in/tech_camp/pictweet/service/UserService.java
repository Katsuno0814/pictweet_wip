package in.tech_camp.pictweet.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.pictweet.entity.UserEntity;
import in.tech_camp.pictweet.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUser(UserEntity userEntity){
    String password = userEntity.getPassword();
    String encodedPassword = passwordEncoder.encode(password);
    userEntity.setPassword(encodedPassword);
    userRepository.insert(userEntity);
  }
}
