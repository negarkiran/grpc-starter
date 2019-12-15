package org.kiran.server;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.kiran.grpc.HelloRequest;
import org.kiran.grpc.HelloResponse;
import org.kiran.grpc.HelloServiceGrpc;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GrpcServerTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    public void shouldRespondWithGreeting() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
        .forName(serverName).directExecutor().addService(new HelloServiceImpl()).build()).start();

        HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        HelloResponse response = blockingStub.hello(HelloRequest.newBuilder()
                .setFirstName("Tom").setLastName("Cruise").build());

        assertEquals("Hello Tom Cruise", response.getGreeting());
    }
}
