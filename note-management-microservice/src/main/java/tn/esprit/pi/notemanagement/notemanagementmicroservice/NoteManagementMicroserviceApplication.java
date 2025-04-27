package tn.esprit.pi.notemanagement.notemanagementmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NoteManagementMicroserviceApplication {

    public static void main(String[] args) {SpringApplication.run(NoteManagementMicroserviceApplication.class, args);
    }

}
