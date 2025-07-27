package com.nutrifit.useraccount.adapter.grpc;

import com.nutrifit.useraccount.dto.UserAccountRequest;
import com.nutrifit.useraccount.dto.UserAccountResponse;
import com.nutrifit.useraccount.mapper.UserAccountGrpcMapper;
import com.nutrifit.useraccount.model.UserAccount;
import com.nutrifit.useraccount.service.UserAccountService;
import com.nutrifit.useraccount.v1.CreateUserAccountRequest;
import com.nutrifit.useraccount.v1.GetUserAccountByIdRequest;
import com.nutrifit.useraccount.v1.GrpcUserAccountResponse;
import com.nutrifit.useraccount.v1.UserAccountServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
//import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;


@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserAccountGrpcService extends UserAccountServiceGrpc.UserAccountServiceImplBase {

    private final UserAccountService userAccountService;

    @Override
    public void createUserAccount(CreateUserAccountRequest createUserAccountRequest, StreamObserver<GrpcUserAccountResponse> responseObserver) {

        log.info("createUserAccount called with request: {}", createUserAccountRequest);

        UserAccountResponse userAccountResponse = userAccountService.createUserAccount(UserAccountGrpcMapper.toDtoRequest(createUserAccountRequest));

        GrpcUserAccountResponse grpcResponse = UserAccountGrpcMapper.toGrpcResponse(userAccountResponse);

        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserAccountById(GetUserAccountByIdRequest request, StreamObserver<GrpcUserAccountResponse> responseObserver) {

        log.info("getUserAccountById called with request: {}", request);

        UserAccountResponse userAccountResponse = userAccountService.getUserAccountById(UUID.fromString(request.getUserId()));

        GrpcUserAccountResponse grpcResponse = UserAccountGrpcMapper.toGrpcResponse(userAccountResponse);

        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }
}
