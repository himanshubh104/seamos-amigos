package com.himansh.seamosamigos.controller;

import com.himansh.seamosamigos.dto.RequestDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.ConnectionService;
import com.himansh.seamosamigos.utility.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/seamos-amigos/")
public class ConnectionsController {

    private final ConnectionService connectionService;
    private int userId = -1;

    public ConnectionsController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @ModelAttribute
    public void fetchUser() {
        userId = CurrentUser.getCurrentUserId();
    }

    @GetMapping(path = "connections/followers")
    public List<UserDto> getUserFollowers() {
        return connectionService.getUserFollowers(userId);
    }

    @GetMapping(path = "connections/followings")
    public List<UserDto> getUserFollowings() {
        return connectionService.getUserFolloiwngs(userId);
    }

    @GetMapping(path = "connections/request/respond")
    public boolean acceptOrRejectRequest(@RequestParam(name = "requestId") int requestId,
                                         @RequestParam(name = "response") char response) throws InAppException {
        return connectionService.acceptOrRejectRequest(userId, requestId, response);
    }

    @GetMapping(path = "connections/request")
    public ResponseEntity<Object> getAllRequests() {
        return ResponseEntity.ok(connectionService.getAllRequests(userId).stream()
                .map(RequestDto::genrateDto).collect(Collectors.toList()));
    }

    @GetMapping(path = "connections/request/create")
    public FollowRequests createRequest(@RequestParam(name = "requestedUser") String requestedUser)
            throws InAppException {
        int requestingUser = userId;
        return connectionService.createRequest(requestedUser, requestingUser);
    }
}
