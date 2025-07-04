package br.edu.utfpr.exemplo.controller;

import br.edu.utfpr.exemplo.config.security.JwtService;
import br.edu.utfpr.exemplo.controller.requests.auth.AuthRequest;
import br.edu.utfpr.exemplo.controller.responses.auth.AuthResponse;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.vo.UserVO;
import br.edu.utfpr.exemplo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserVO> register(@RequestBody UserVO userVO) {
        User user = modelMapper.map(userVO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        userVO.setId(user.getId());
        return new ResponseEntity<>(userVO, HttpStatus.CREATED);
    }
}

