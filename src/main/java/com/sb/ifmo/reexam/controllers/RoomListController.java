package com.sb.ifmo.reexam.controllers;

import com.sb.ifmo.reexam.data.*;
import com.sb.ifmo.reexam.requests.CreateRoomRequest;
import com.sb.ifmo.reexam.requests.InviteRequest;
import net.minidev.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomListController {
    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MessageRepository messageRepository;

    // tested
    @GetMapping()
    public String roomSet(@AuthenticationPrincipal OAuth2User principal) {
        CustomUser userByPrincipal = customUserRepository.findByEmailIs(principal.getAttribute("email"));
        JSONObject response = new JSONObject();
        Set<String> availableRooms = roomRepository.findAllByUsersContainsOrIsPrivate(userByPrincipal, false).stream().map(Room::toString).collect(Collectors.toSet());
        response.put("availableRooms", availableRooms);

        return response.toString();
    }

    // tested
    @PostMapping("/create")
    public String roomCreate(@AuthenticationPrincipal OAuth2User principal, @RequestBody CreateRoomRequest requestBody) {
        CustomUser userByPrincipal = customUserRepository.findByEmailIs(principal.getAttribute("email"));
        Room newRoom = new Room(requestBody.getName(), requestBody.isPrivate(), userByPrincipal);
        roomRepository.save(newRoom);

        JSONObject response = new JSONObject();
        response.put("room_id", newRoom.getId());

        return response.toString();
    }
}
