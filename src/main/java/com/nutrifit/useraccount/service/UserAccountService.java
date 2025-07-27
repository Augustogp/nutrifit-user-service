package com.nutrifit.useraccount.service;

import com.nutrifit.useraccount.dto.UserAccountRequest;
import com.nutrifit.useraccount.dto.UserAccountResponse;
import com.nutrifit.useraccount.exception.ResourceNotFoundException;
import com.nutrifit.useraccount.mapper.UserAccountMapper;
import com.nutrifit.useraccount.model.UserAccount;
import com.nutrifit.useraccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;


    public List<UserAccountResponse> getAllUserAccounts() {

        return userAccountRepository.findAll()
                .stream()
                .map(UserAccountMapper::toResponse)
                .toList();
    }

    public UserAccountResponse getUserAccountById(UUID id) {

        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Account with ID: " + id + " was not found."));

        return UserAccountMapper.toResponse(userAccount);
    }

    public UserAccountResponse createUserAccount(UserAccountRequest userAccountRequest) {

        UserAccount savedUserAccount = userAccountRepository.save(UserAccountMapper.toEntity(userAccountRequest));

        return UserAccountMapper.toResponse(savedUserAccount);
    }

    public UserAccountResponse updateUserAccount(UUID id, UserAccountRequest userAccountRequest) {

        UserAccount existingUserAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Account with ID: " + id + " was not found."));

        existingUserAccount.setUsername(userAccountRequest.username());
        existingUserAccount.setPassword(userAccountRequest.password());
        existingUserAccount.setFirstName(userAccountRequest.firstName());
        existingUserAccount.setLastName(userAccountRequest.lastName());
        existingUserAccount.setEmail(userAccountRequest.email());

        UserAccount updatedUserAccount = userAccountRepository.save(existingUserAccount);

        return UserAccountMapper.toResponse(updatedUserAccount);
    }

    public void deleteUserAccountById(UUID id) {

        userAccountRepository.findById(id).ifPresentOrElse(
            userAccountRepository::delete,
            () -> {
                throw new ResourceNotFoundException("User Account with ID: " + id + " was not found.");
            }
        );
    }
}
