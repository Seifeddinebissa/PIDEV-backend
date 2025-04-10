package tn.esprit.gestionforum_discussion.user;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.prefix}/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserDTO> login(@RequestBody final UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO));
    }


    @MessageMapping("/user/connect") // Receives message from clients sending to /app/user/connect
    @SendTo("/topic/active") // Send the response to all clients subscribe to /topic/active
    public UserDTO connect(@RequestBody UserDTO userDTO) {
        return userService.connect(userDTO);
    }



    @MessageMapping("/user/disconnect") // Receives message from clients sending to /app/user/disconnect
    @SendTo("/topic/active") // Send the response to all clients subscribe to /topic/active
    public UserDTO disconnect(@RequestBody UserDTO userDTO) {
        return userService.logout(userDTO.getUsername());
    }


    @GetMapping("/online")
    public ResponseEntity<List<UserDTO>> getOnlineUsers() {
        return ResponseEntity.ok(userService.getOnlineUsers());
    }

}
