package com.me_social.MeSocial.mapper;

import com.me_social.MeSocial.entity.dto.response.ChatResponse;
import com.me_social.MeSocial.entity.dto.response.MessageResponse;
import com.me_social.MeSocial.entity.modal.Chat;
import com.me_social.MeSocial.entity.modal.Message;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMapper {

    UserMapper userMapper;

    public ChatResponse toChatResponse(Chat chat) {
        ChatResponse chatResponse = new ChatResponse();

        chatResponse.setChatName(chat.getChatName());
        chatResponse.setId(chat.getId());
        chatResponse.setGroup(chat.isGroup());
        chatResponse.setUsers(chat.getUsers().stream().map(userMapper::toUserResponse).collect(Collectors.toSet()));
        chatResponse.setAdmins(chat.getAdmins().stream().map(userMapper::toUserResponse).collect(Collectors.toSet()));
        chatResponse.setMessages(chat.getMessages().stream().map(this::toMessageResponse).toList());
        chatResponse.setChatImage(chat.getChatImage());
        chatResponse.setCreatedBy(userMapper.toUserResponse(chat.getCreatedBy()));

        return chatResponse;
    }

    public MessageResponse toMessageResponse(Message message) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(message.getId());
        messageResponse.setSender(userMapper.toUserResponse(message.getUser()));
        messageResponse.setContent(message.getContent());
        messageResponse.setChatId(message.getChat().getId());
        messageResponse.setTimestamp(message.getTimestamp());

        return messageResponse;
    }

}
