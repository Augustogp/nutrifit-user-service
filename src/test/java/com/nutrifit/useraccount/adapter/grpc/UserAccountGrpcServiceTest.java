package com.nutrifit.useraccount.adapter.grpc;

import com.nutrifit.useraccount.TestcontainersConfiguration;
import com.nutrifit.useraccount.v1.CreateUserAccountRequest;
import com.nutrifit.useraccount.v1.UserAccountServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/testdata/create_schema_and_insert_user_accounts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class UserAccountGrpcServiceTest {

    @Value("${grpc.server.port}")
    private int grpc_port;

    private static UserAccountServiceGrpc.UserAccountServiceBlockingStub stub;

    @BeforeEach
    void setUp() {
        waitForGrpcStartup(grpc_port, Duration.ofSeconds(20));

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", grpc_port)
                .usePlaintext()
                .build();

        stub = UserAccountServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void shouldCreateUser_whenGrpcRequestIsValid() {
        var request = CreateUserAccountRequest.newBuilder()
                .setUsername("grpcuser")
                .setPassword("secure123")
                .setFirstName("GRPC")
                .setLastName("User")
                .setEmail("grpcuser@nutrifit.com")
                .build();

        var response = stub.createUserAccount(request);

        assertEquals("grpcuser", response.getUsername());
        assertEquals("GRPC", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("grpcuser@nutrifit.com", response.getEmail());
    }

    @Test
    public void shouldGetUserById_whenGrpcRequestIsValid() {
        var request = com.nutrifit.useraccount.v1.GetUserAccountByIdRequest.newBuilder()
                .setUserId("f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0")
                .build();

        var response = stub.getUserAccountById(request);

        assertEquals("f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0", response.getUserId());
    }

    @Test
    public void shouldReturnNotFound_whenUserDoesNotExist() {
        var request = com.nutrifit.useraccount.v1.GetUserAccountByIdRequest.newBuilder()
                .setUserId("00000000-0000-0000-0000-000000000000")
                .build();

        try {
            stub.getUserAccountById(request);
        } catch (io.grpc.StatusRuntimeException e) {
            assertEquals(io.grpc.Status.NOT_FOUND.getCode(), e.getStatus().getCode());
        }
    }

    private static void waitForGrpcStartup(int port, Duration timeout) {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            try (Socket socket = new Socket("localhost", port)) {
                return; // gRPC server is ready
            } catch (IOException ignored) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for gRPC startup", e);
                }
            }
        }
        throw new RuntimeException("gRPC server did not start on port " + port + " within " + timeout.getSeconds() + " seconds");
    }
}
