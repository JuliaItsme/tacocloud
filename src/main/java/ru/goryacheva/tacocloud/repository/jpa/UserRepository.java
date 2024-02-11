package ru.goryacheva.tacocloud.repository.jpa;
import org.springframework.data.repository.CrudRepository;
import ru.goryacheva.tacocloud.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

  User findByUsername(String username);
}
