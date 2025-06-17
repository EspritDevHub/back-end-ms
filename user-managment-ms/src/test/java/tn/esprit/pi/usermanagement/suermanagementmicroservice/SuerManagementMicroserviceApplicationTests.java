package tn.esprit.pi.usermanagement.suermanagementmicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
@SpringBootTest
@EnableAutoConfiguration(exclude = {
  org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
  org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class
})
class SuerManagementMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }

}
