package doktoree.backend.security;

import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.mapper.UserMapper;
import doktoree.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;


    }

    public ResponseEntity<?> register(RegisterDto registerDto) {

        Optional<User> optionalUser = userRepository.findByEmail(registerDto.getEmail());

        if (optionalUser.isPresent()) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);

        }
        Employee employee = new Employee();
        employee.setId(registerDto.getEmployeeId());
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setEmployee(employee);

        userRepository.save(user);

        return new ResponseEntity<>("Successfully registration!", HttpStatus.CREATED);


    }

    public ResponseEntity<Map<String, Object>> login(LoginDto loginDto) {

        Optional<User> optionalUser = userRepository
                .findByEmail(loginDto.getEmail());

        if(optionalUser.isEmpty()){

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Wrong password or email!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            Map<String, Object> wrongPasswordMap = new HashMap<>();
            wrongPasswordMap.put("message", "Wrong password or email!");
            return new ResponseEntity<>(wrongPasswordMap, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("user", UserMapper.mapToUserDto(user));
        return new ResponseEntity<>(tokenMap, HttpStatus.OK);

    }

}
