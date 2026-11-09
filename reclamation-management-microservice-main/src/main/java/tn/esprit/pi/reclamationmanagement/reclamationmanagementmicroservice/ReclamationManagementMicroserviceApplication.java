package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.FeignClient")



public class ReclamationManagementMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReclamationManagementMicroserviceApplication.class, args);
    }

}
