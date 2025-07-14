package doktoree.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {


    private UserService userService;

    @Autowired
    public AuthController(UserService userService){

        this.userService = userService;

    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){

        return userService.register(registerDto);

    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

        return userService.login(loginDto);

    }


}
