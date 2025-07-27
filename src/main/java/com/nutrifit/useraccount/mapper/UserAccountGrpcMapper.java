package com.nutrifit.useraccount.mapper;

import com.nutrifit.useraccount.dto.UserAccountRequest;
import com.nutrifit.useraccount.v1.CreateUserAccountRequest;
import com.nutrifit.useraccount.v1.GrpcUserAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class UserAccountGrpcMapper {

    public static GrpcUserAccountResponse toGrpcResponse(com.nutrifit.useraccount.dto.UserAccountResponse userAccountResponse) {
        return GrpcUserAccountResponse.newBuilder()
                .setUserId(userAccountResponse.id().toString())
                .setUsername(userAccountResponse.username())
                .setFirstName(userAccountResponse.firstName())
                .setLastName(userAccountResponse.lastName())
                .setEmail(userAccountResponse.email())
                .build();
    }

    public static UserAccountRequest toDtoRequest(CreateUserAccountRequest createUserAccountRequest) {
        return new UserAccountRequest(
                createUserAccountRequest.getUsername(),
                createUserAccountRequest.getPassword(),
                createUserAccountRequest.getFirstName(),
                createUserAccountRequest.getLastName(),
                createUserAccountRequest.getEmail()
        );
    }
}
