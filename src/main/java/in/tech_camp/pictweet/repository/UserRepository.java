package in.tech_camp.pictweet.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.pictweet.entity.UserEntity;

@Mapper
public interface UserRepository {
  @Insert("INSERT INTO users (nickname, email, password) VALUES (#{nickname}, #{email}, #{password})")
  void insert(UserEntity user);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);

  @Select("SELECT * FROM users WHERE id = #{id}")
  @Results(value = {
    @Result(property = "id", column = "id"), 
    @Result(property = "tweets", column = "id", 
            many = @Many(select = "in.tech_camp.pictweet.repository.TweetRepository.findByUserId")),
    @Result(property = "comments", column = "id", 
            many = @Many(select = "in.tech_camp.pictweet.repository.CommentRepository.findByUserId"))
  })
  UserEntity findById(Integer id);
}
