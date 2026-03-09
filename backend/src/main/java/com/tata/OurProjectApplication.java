package com.tata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tata.config.AppConstant;
import com.tata.entity.Role;
import com.tata.repo.RoleRepository;

@SpringBootApplication
public class OurProjectApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(OurProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// System.out.println("Encoded pass: "+this.passwordEncoder.encode("1234"));

		try {
			Role role01 = new Role();
			role01.setRoleId(AppConstant.ADMIN_USER);
			role01.setRoleName("ADMIN");

			Role role02 = new Role();
			role02.setRoleId(AppConstant.NORMAL_USER);
			role02.setRoleName("USER");

			List<Role> roles = List.of(role01, role02);

			List<Role> saveRoles = this.roleRepository.saveAll(roles);

			saveRoles.forEach(role -> {
				System.out.println(role.getRoleName());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
