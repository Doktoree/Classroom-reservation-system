package doktoree.backend.security;

import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
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
    public UserService(UserRepository userRepository, JwtUtil jwtUtil){

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;


    }

    public ResponseEntity<?> register(RegisterDto registerDto){

        Optional<User> optionalUser = userRepository.findByEmail(registerDto.getEmail());

        if(!optionalUser.isEmpty())
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);

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

    public ResponseEntity<?> login(LoginDto loginDto){

        User user = userRepository
                .findByEmail(loginDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("There is no user with given email!"));

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            Map<String, String> wrongPasswordMap = new HashMap<>();
            wrongPasswordMap.put("message", "Wrong password!");
            return new ResponseEntity<>(wrongPasswordMap, HttpStatus.UNAUTHORIZED);


        }

        String token = jwtUtil.generateToken(user);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return new ResponseEntity<>(tokenMap, HttpStatus.OK);

    }

}
