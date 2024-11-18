package com.example.chating2.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${react.url}")
    private String reactUrl;

    @Value("${broker.topic}")
    private String topicPrefix;

    @Value("${app.destinationPrefix}")
    private String appDestinationPrefix;

    @Value("${websocket.endpoint}")
    private String websocketEndpoint;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(topicPrefix); // 메시지 전송을 위한 브로커 경로 설정
        config.setApplicationDestinationPrefixes(appDestinationPrefix); // 클라이언트에서 보낼 메시지 경로 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(websocketEndpoint) // 웹소켓 엔드포인트 설정
                .setAllowedOrigins(reactUrl) // CORS 허용
                .withSockJS(); // SockJS 사용
    }
}




