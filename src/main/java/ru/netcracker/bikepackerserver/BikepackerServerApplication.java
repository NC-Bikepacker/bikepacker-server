package ru.netcracker.bikepackerserver;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class BikepackerServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BikepackerServerApplication.class, args);
    }
}
