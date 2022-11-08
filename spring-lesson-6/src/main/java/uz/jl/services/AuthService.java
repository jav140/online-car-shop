package uz.jl.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.AuthRole;
import uz.jl.domains.AuthUser;
import uz.jl.dto.LoginDto;
import uz.jl.dto.UserDto;
import uz.jl.repository.AuthRepository;
import uz.jl.repository.AuthRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final AuthRoleRepository authRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        return new UserDetails(authUser);
    }

    public void register(UserDto userDto, BindingResult result) {

        AuthRole authRole = authRoleRepository.findByCode("USER");
        List<AuthRole> roleList = new ArrayList<>();
        roleList.add(authRole);
        int userListSize = authRepository.findAll().size();

        Optional<AuthUser> user = authRepository.findByUsername(userDto.getUsername());

        if (Objects.nonNull(user)) {
            result.rejectValue("username","error.user","username already exists");
//            ObjectError objectError = new ObjectError("User", "username already exists");
//            result.addError(objectError);
        }

        AuthUser authUser = AuthUser.builder()
                .id((long)userListSize+1)
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .active(true)
                .roles(roleList)
                .build();

        authRepository.save(authUser);

    }

    public void login(LoginDto loginDto, BindingResult result) {
        Optional<AuthUser> authUser = authRepository.findByUsername(loginDto.getUsername());

        if(Objects.isNull(authUser)){
            ObjectError error = new ObjectError("user","User not found");
            result.addError(error);
        }

    }

    public List<AuthUser> findAll() {
        return authRepository.findAll();
    }
}
