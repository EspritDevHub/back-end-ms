package tn.esprit.pi.usermanagement.suermanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUseRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    User getUserByEmail(String email);
    User getUserByEspritId(String espritId);
    List<User> getUsersByClassName(String className);
}
