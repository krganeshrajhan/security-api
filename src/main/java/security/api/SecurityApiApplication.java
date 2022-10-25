package security.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import security.api.entity.Role;
import security.api.entity.User;
import security.api.service.UserService;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null, "Ganesh Rajhan", "ganesh", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "Tom Cruise", "tom", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "Jim Carry", "jim", "1234", new ArrayList<>()));

            userService.addRoleToUser("ganesh", "ROLE_USER");
            userService.addRoleToUser("ganesh", "ROLE_MANAGER");
            userService.addRoleToUser("ganesh", "ROLE_ADMIN");

            userService.addRoleToUser("tom", "ROLE_USER");
            userService.addRoleToUser("tom", "ROLE_MANAGER");
            userService.addRoleToUser("tom", "ROLE_ADMIN");
        };
    }
}
