package com.nutrifit.useraccount.mapper;

import com.nutrifit.useraccount.dto.UserAccountRequest;
import com.nutrifit.useraccount.dto.UserAccountResponse;
import com.nutrifit.useraccount.model.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {

    public static UserAccount toEntity(UserAccountRequest userAccountRequest) {

        return UserAccount.builder()
                .username(userAccountRequest.username())
                .email(userAccountRequest.email())
                .password(userAccountRequest.password())
                .firstName(userAccountRequest.firstName())
                .lastName(userAccountRequest.lastName())
                .build();
    }

    public static UserAccountResponse toResponse(UserAccount userAccount) {

        return new UserAccountResponse(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getFirstName(),
                userAccount.getLastName(),
                userAccount.getEmail()
        );
    }
}
