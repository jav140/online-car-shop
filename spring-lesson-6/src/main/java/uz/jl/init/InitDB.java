package uz.jl.init;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.jl.domains.AuthRole;
import uz.jl.domains.AuthUser;
import uz.jl.repository.AuthPermissionRepository;
import uz.jl.repository.AuthRepository;
import uz.jl.repository.AuthRoleRepository;

import java.util.Collections;
import java.util.List;

//@Component
public class InitDB implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AuthRoleRepository authRoleRepository = context.getBean(AuthRoleRepository.class);
        AuthPermissionRepository authPermissionRepository = context.getBean(AuthPermissionRepository.class);
        AuthRepository repository = context.getBean(AuthRepository.class);
        authPermissionRepository.deleteAll();
        authRoleRepository.deleteAll();
        repository.deleteAll();
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        AuthUser superUser = new AuthUser();
        superUser.setUsername("superuser");
        superUser.setPassword(passwordEncoder.encode("123"));
        superUser.setActive(true);

        AuthUser managerUser = new AuthUser();
        managerUser.setUsername("manager");
        managerUser.setPassword(passwordEncoder.encode("123"));
        managerUser.setActive(true);


        AuthRole admin = new AuthRole();
        admin.setCode("ADMIN");
        admin.setName("Admin");

        AuthRole manager = new AuthRole();
        manager.setCode("MANAGER");
        manager.setName("Manager");


        superUser.getRoles().add(admin);
        superUser.getRoles().add(manager);
        managerUser.setRoles(Collections.singletonList(manager));
        repository.saveAll(List.of(superUser, managerUser));

    }
}
