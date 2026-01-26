package com.judy.ecommerce.backend;

import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase implements InitializingBean {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    public InitDatabase(UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /// Failsafe to ensure that an admin user always exists when starting the app
    @Override
    public void afterPropertiesSet() throws Exception {
        if (userRepository.existsByRoleAndDisabledFalse(RoleEnum.ADMIN)) {
            return;
        }

        Users adminUser = userRepository.findByEmailIgnoreCase(environment.getRequiredProperty("ADMIN_EMAIL"))
                .orElse(new Users());

        adminUser.setLastname("ADMIN");
        adminUser.setFirstname("ADMIN");
        adminUser.setEmail(environment.getRequiredProperty("ADMIN_EMAIL"));
        adminUser.setPassword(passwordEncoder.encode(environment.getRequiredProperty("ADMIN_PASSWORD")));
        adminUser.setRole(RoleEnum.ADMIN);
        adminUser.setDisabled(false);

        userRepository.save(adminUser);
    }
}
