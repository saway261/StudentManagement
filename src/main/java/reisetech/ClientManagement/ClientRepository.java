package reisetech.ClientManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClientRepository {

  @Select("SELECT name, age FROM client WHERE name = #{name}")
  Client selectByName(String name);

  @Select("SELECT name, age FROM client")
  List<Client> selectAllClients();

  @Insert("INSERT INTO client (name, age) VALUES (#{name}, #{age})")
  void createClient(String name, int age);

  @Update("UPDATE client SET age = #{age} WHERE name = #{name}")
  void updateClient(String name, int age);

  @Delete("DELETE FROM client WHERE name = #{name}")
  void deleteClient(String name);


}
