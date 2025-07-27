package com.nutrifit.useraccount.controller;

import com.nutrifit.useraccount.dto.UserAccountRequest;
import com.nutrifit.useraccount.dto.UserAccountResponse;
import com.nutrifit.useraccount.mapper.UserAccountMapper;
import com.nutrifit.useraccount.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<List<UserAccountResponse>> getUserAccounts() {

        List<UserAccountResponse> userAccounts = userAccountService.getAllUserAccounts();

        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResponse> getUserAccountById(@PathVariable UUID id) {

        UserAccountResponse userAccountResponse = userAccountService.getUserAccountById(id);

        return ResponseEntity.ok(userAccountResponse);
    }

    @PostMapping
    public ResponseEntity<UserAccountResponse> createUserAccount(@Valid @RequestBody UserAccountRequest userAccountRequest) {

        UserAccountResponse userAccountResponse = userAccountService.createUserAccount(userAccountRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userAccountResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponse> updateUserAccount(@PathVariable UUID id, @RequestBody UserAccountRequest userAccountRequest) {

        UserAccountResponse updatedUserAccount = userAccountService.updateUserAccount(id, userAccountRequest);

        return ResponseEntity.ok(updatedUserAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccountById(@PathVariable UUID id) {

        userAccountService.deleteUserAccountById(id);

        return ResponseEntity.noContent().build();
    }
}
