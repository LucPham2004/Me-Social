package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.ChatRequest;
import com.me_social.MeSocial.entity.dto.response.ChatResponse;
import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class ChatMapper {

    UserRepository userRepository;
    
    public DirectMessage toDirectMessage(ChatRequest request) {
        DirectMessage message = new DirectMessage();
        message.setContent(request.getContent());
        message.setReceiver(userRepository.findById(request.getReceiverId()).get());
        message.setSender(userRepository.findById(request.getSenderId()).get());
        message.setRead(false);

        return message;
    }

    public ChatResponse toResponse(DirectMessage message) {
        ChatResponse response = new ChatResponse();
        response.setContent(message.getContent());
        response.setReceiverId(message.getReceiver().getId());
        response.setSenderId(message.getSender().getId());
        response.setIsRead(false);
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        return response;
    }
}