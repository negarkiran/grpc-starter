package org.kiran.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.kiran.grpc.HelloRequest;
import org.kiran.grpc.HelloResponse;
import org.kiran.grpc.HelloServiceGrpc;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub =
                HelloServiceGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                .setFirstName("Tom")
                .setLastName("Cruise")
                .build());

        System.out.println("Response received from server: \n" + helloResponse);
        channel.shutdown();
    }
}
