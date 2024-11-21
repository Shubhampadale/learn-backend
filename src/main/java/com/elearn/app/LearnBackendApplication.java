package com.elearn.app;

import com.elearn.app.config.AppConstants;
import com.elearn.app.entities.Role;
import com.elearn.app.entities.User;
import com.elearn.app.repositories.RolesRepo;
import com.elearn.app.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class LearnBackendApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LearnBackendApplication.class, args);
	}

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RolesRepo rolesRepo;

	@Override
	public void run(String... args) throws Exception {



		Role role1 = new Role();
		role1.setRoleName(AppConstants.ROLE_ADMIN);
		role1.setRoleId(UUID.randomUUID().toString());

		Role role2 = new Role();
		role2.setRoleName(AppConstants.ROLE_GUEST);
		role2.setRoleId(UUID.randomUUID().toString());

		//creating admin role if not exists
		rolesRepo.findByRoleName(AppConstants.ROLE_ADMIN).ifPresentOrElse(

				role-> {

					System.out.println(role.getRoleName() + " already present..");
				},
				()->{
					rolesRepo.save(role1);
				}
		);

		rolesRepo.findByRoleName(AppConstants.ROLE_GUEST).ifPresentOrElse(

				role-> {

					System.out.println(role.getRoleName() + " already present..");
				},
				()->{
					rolesRepo.save(role2);
				}
		);

		User user = new User();

		user.setName("Shubham");
		user.setUserId(UUID.randomUUID().toString());
		user.setEmail("abc@gmail.com");
		user.setPassword(passwordEncoder.encode("abc"));
		user.setActive(true);
		user.setCreateAt(new Date());
		user.setEmailVerified(true);
		user.setAbout("This is a normal user");

		user.assignRole(role1);
		user.assignRole(role2);

		userRepo.findByEmail("abc@gmail.com").ifPresentOrElse(user1-> {

					System.out.println("The user is alredy exists: "+user.getEmail());
				}, ()->{

					userRepo.save(user);
					System.out.println("User is created!!");
				}
				);


		User user1 = new User();

		user1.setName("Parth");
		user1.setUserId(UUID.randomUUID().toString());
		user1.setEmail("xyz@gmail.com");
		user1.setPassword(passwordEncoder.encode("xyz"));
		user1.setActive(true);
		user1.setCreateAt(new Date());
		user1.setEmailVerified(true);
		user1.setAbout("This is a normal user");

		user1.assignRole(role2);

		userRepo.findByEmail("xyz@gmail.com").ifPresentOrElse(user_1-> {

					System.out.println("The user is alredy exists: "+user1.getEmail());
				}, ()->{

					userRepo.save(user1);
					System.out.println("User is created!!");
				}
		);

	}
}
