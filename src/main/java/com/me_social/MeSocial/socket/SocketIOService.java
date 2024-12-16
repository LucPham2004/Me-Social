package com.me_social.MeSocial.socket;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.me_social.MeSocial.entity.modal.ChatMessage;
import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.UserRepository;
import com.me_social.MeSocial.service.DirectMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketIOService {

    DirectMessageService messageService;
    UserRepository userRepository;

    public void sendSocketmessage(SocketIOClient senderClient, ChatMessage message, String room) {
        for (
            SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }

    public void saveMessage(SocketIOClient senderClient, ChatMessage message, String room) {

        messageService.saveDirectMessage(
            DirectMessage.builder()
                .content(message.getContent())
                .sender(userRepository.findById(message.getSenderId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED)))
                .receiver(userRepository.findById(message.getReceiverId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED)))
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build()
        );

        sendSocketmessage(senderClient, message, room);

    }

    // public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
    //     ChatMessage storedMessage = messageService.saveMessage(
    //         Message.builder()
    //             .messageType(MessageType.SERVER)
    //             .message(message)
    //             .room(room)
    //             .build()
    //     );

    //     sendSocketmessage(senderClient, storedMessage, room);
    // }
}
