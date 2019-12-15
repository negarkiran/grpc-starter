package org.kiran.server;

import io.grpc.stub.StreamObserver;
import org.kiran.grpc.HelloRequest;
import org.kiran.grpc.HelloResponse;
import org.kiran.grpc.HelloServiceGrpc.HelloServiceImplBase;

public class HelloServiceImpl extends HelloServiceImplBase {

    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseStreamObserver) {
        System.out.println("Request received from client: \n" + request);

        String greeting = "Hello " + request.getFirstName() + " " + request.getLastName();

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
