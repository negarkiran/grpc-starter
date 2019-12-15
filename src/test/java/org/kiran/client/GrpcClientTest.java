package org.kiran.client;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.kiran.grpc.HelloRequest;
import org.kiran.grpc.HelloResponse;
import org.kiran.grpc.HelloServiceGrpc;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GrpcClientTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final HelloServiceGrpc.HelloServiceImplBase serviceImpl =
            mock(HelloServiceGrpc.HelloServiceImplBase.class, delegatesTo(
                    new HelloServiceGrpc.HelloServiceImplBase() {
                        @Override
                        public void hello(HelloRequest request, StreamObserver<HelloResponse> respObserver) {
                        respObserver.onNext(HelloResponse.getDefaultInstance());
                        respObserver.onCompleted();
                         }
                    }));

    @Test
    public void shouldVerifyClientRequest() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
        .forName(serverName).directExecutor().addService(serviceImpl).build()).start();

        ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder
        .forName(serverName).directExecutor().build());

        HelloServiceGrpc.HelloServiceBlockingStub stub =
                HelloServiceGrpc.newBlockingStub(channel);

        ArgumentCaptor<HelloRequest> requestCaptor = ArgumentCaptor.forClass(HelloRequest.class);

        stub.hello(HelloRequest.newBuilder().setFirstName("Tom").setLastName("Cruise").build());

        verify(serviceImpl).hello(requestCaptor.capture(), ArgumentMatchers.<StreamObserver<HelloResponse>> any());

        assertEquals("Tom", requestCaptor.getValue().getFirstName());
    }
}
