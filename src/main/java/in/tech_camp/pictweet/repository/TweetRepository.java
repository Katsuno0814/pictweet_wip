package in.tech_camp.pictweet.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.pictweet.entity.TweetEntity;

@Mapper
public interface TweetRepository {
  
  @Select("SELECT * FROM tweets")
  List<TweetEntity> findAll();

  @Insert("INSERT INTO tweets (name, text, image, user_id) VALUES (#{name}, #{text}, #{image}, #{userId})")
  void insert(TweetEntity tweet);

  @Delete("DELETE FROM tweets WHERE id = #{id}")
  void deleteById(Integer id);

  @Select("SELECT * FROM tweets WHERE id = #{id}")
  TweetEntity findById(Integer id);

  @Update("UPDATE tweets SET name = #{name}, text = #{text}, image = #{image} WHERE id = #{id}")
  void update(TweetEntity tweet);
}
