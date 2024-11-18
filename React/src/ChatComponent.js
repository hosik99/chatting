import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const ChatApp = () => {
  const [messages, setMessages] = useState([]);  // 채팅 메시지 목록 저장
  const [inputMessage, setInputMessage] = useState('');     // 입력 중인 메시지
  const [messageOutput, setMessageOutput] = useState({ username: "Smith", text: "", roomId: 1234, channelId: 111, });  ///서버에 보낼 메세지 객체

  const messagesEndRef = useRef(null);            // 스크롤 이동을 위한 ref
  const stompClientRef = useRef(null); 

  useEffect(() => {
    // WebSocket 클라이언트 설정
    const stompClient = new Client({
      brokerURL: 'http://localhost:8080/chat',  // WebSocket 연결 URL
      connectHeaders: {},

      // 메시지 수신 시
      onConnect: () => { // frame -> STOMP 프로토콜에서 서버로부터 받은 STOMP 프레임
        console.log("메시지 수신");
        stompClient.subscribe(`/topic/${messageOutput.channelId}/${messageOutput.roomId}`, (messageOutput) => { //messageOutput->클라이언트에서 받은 메시지 내용
          setMessages((prevMessages) => [ ...prevMessages,
            JSON.parse(messageOutput.body),
          ]);
        });
      },

      // WebSocket 연결에서 문제가 발생
      onWebSocketError: (error) => {    //네트워크 오류, 서버가 응답하지 않는 경우, 연결이 끊어진 경우
        console.error('Error with websocket', error);
      },

      // STOMP 프로토콜에서 발생한 에러를 처리
      onStompError: (frame) => {
        console.error('STOMP error', frame);
      },
      
      //webSocketFactory -> STOMP 클라이언트가 실제 WebSocket 연결을 만들 때 어떤 방법으로 연결할지 결정하는 역할
      webSocketFactory: () => new SockJS('http://localhost:8080/chat'),  // SockJS WebSocket 연결
    });

    stompClientRef.current = stompClient; // stompClient를 ref로 저장
    stompClient.activate();  // WebSocket 연결 활성화

    // 클린업: 컴포넌트 언마운트 시 WebSocket 연결 해제
    return () => stompClient.deactivate();
  }, []);

  useEffect(() => {
    // 메시지가 추가될 때마다 마지막 메시지로 스크롤
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  //메세지 송신
  const sendMessage = () => {
    if (inputMessage.trim() !== '') {
      const newMessage = { 
        username: messageOutput.username, 
        text: inputMessage, 
        channelId: messageOutput.channelId,
        roomId : messageOutput.roomId
      };
      
      // 메시지 전송
      const stompClient = stompClientRef.current; 
      if (stompClient) {
        stompClient.publish({
          destination: `/app/receive`,  // 서버의 @MessageMapping 경로
          body: JSON.stringify(newMessage),
        });
      }

      setInputMessage('');
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>Chat App</h1>
      <div
        style={{
          height: '300px',
          overflowY: 'scroll',
          border: '1px solid #ccc',
          marginBottom: '10px',
        }}
      >
        {/* 채팅 메시지 표시 영역 */}
        {messages.map((msg, index) => (
          <div key={index} style={{ marginBottom: '10px' }}>
            <strong>{msg.username}:</strong> {msg.text}
          </div>
        ))}
        <div ref={messagesEndRef} />  {/* 메시지 끝에 위치한 빈 div */}
      </div>

      {/* 메시지 입력창 */}
      <input
        type="text"
        value={inputMessage}
        onChange={(e) => setInputMessage(e.target.value)}
        placeholder="Type a message"
        style={{ width: '300px', padding: '10px' }}
      />
      <button
        onClick={sendMessage}
        style={{
          padding: '10px 20px',
          marginLeft: '10px',
          cursor: 'pointer',
          backgroundColor: '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
        }}
      >
        Send
      </button>
      <button onClick={()=> setMessageOutput((prevState) => ({ ...prevState, username: "Smith" }))}>Smith</button>
      <button onClick={()=> setMessageOutput((prevState) => ({ ...prevState, username: "Sam" }))}>Sam</button>
    </div>
  );
};

export default ChatApp;
