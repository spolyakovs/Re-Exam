package com.sb.ifmo.reexam.controllers;

import com.sb.ifmo.reexam.data.*;
import com.sb.ifmo.reexam.requests.CreateMessageRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/{room_id}/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomMessagesController {
    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public String roomMessages(@AuthenticationPrincipal OAuth2User principal, @PathVariable long room_id) {
        CustomUser userByPrincipal = customUserRepository.findByEmailIs(principal.getAttribute("email"));
        Room room = roomRepository.findById(room_id);
        if (room.isAvailabe(userByPrincipal)) {
            JSONObject response = new JSONObject();
            response.put("messages", room.getMessages().toString());
            return response.toString();
        } else {
            return "{\"error\":\"This room is private\"}";
        }
    }

    @GetMapping("{username}")
    public String roomMessagesByUsername(@AuthenticationPrincipal OAuth2User principal, @PathVariable long room_id, @PathVariable String username) {
        CustomUser userByPrincipal = customUserRepository.findByEmailIs(principal.getAttribute("email"));
        CustomUser user = customUserRepository.findByUsernameIs(username);
        Room room = roomRepository.findById(room_id);
        if (user != null) {
            if (room.isAvailabe(userByPrincipal)) {
                JSONObject response = new JSONObject();
                Set<String> messages = messageRepository.findAllByRoomAndUser(room, user).stream().map(Message::toString).collect(Collectors.toSet());
                response.put("messages", messages);
                return response.toString();
            } else {
                return "{\"error\":\"This room is private\"}";
            }
        } else {
            return "{\"error\":\"This user does not exist\"}";
        }

    }

    @PostMapping("/create")
    public String messageCreate(@AuthenticationPrincipal OAuth2User principal, @PathVariable long room_id, @RequestBody CreateMessageRequest requestBody) {
        CustomUser userByPrincipal = customUserRepository.findByEmailIs(principal.getAttribute("email"));
        Room room = roomRepository.findById(room_id);
        Message newMessage = new Message(requestBody.getText(), room, userByPrincipal);
        messageRepository.save(newMessage);

        return "{\"message\": \"Message was successfully posted\"}";
    }
}