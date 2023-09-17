package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.LoginRequest;
import ra.model.dto.request.RegisterRequest;
import ra.model.dto.response.JwtResponse;
import ra.security.jwt.JwtProvider;
import ra.security.user_principle.UserPrinciple;
import ra.service.IUserService;
import ra.service.impl.MailService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v4/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MailService mailService;
    @GetMapping("")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("vaof dc");
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signin(@RequestBody LoginRequest formSignInDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(formSignInDto.getUsername(),formSignInDto.getPassword())
        ); // tạo đối tương authentiction để xác thực thông qua username va password
        // tạo token và trả về cho người dùng
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userPrinciple);
        // lấy ra user principle
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(JwtResponse.builder().token(token)
                .name(userPrinciple.getName())
                .username(userPrinciple.getUsername())
                .roles(roles)
                .type("Bearer")
                .status(userPrinciple.isStatus()).build());
    }
    @PostMapping("/register")
    private ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest){
        userService.save(registerRequest);
        mailService.sendMail(registerRequest.getEmail(),"success","Bạn đã đăng ký thành công tài khoản :😁😁😁😁");
        return new ResponseEntity("register success", HttpStatus.CREATED);
    }
}