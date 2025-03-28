package com.me_social.MeSocial.service;

import com.me_social.MeSocial.entity.dto.request.SendMessageRequest;
import com.me_social.MeSocial.entity.modal.Chat;
import com.me_social.MeSocial.entity.modal.Message;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
    MessageRepository messageRepository;
    UserService userService;
    ChatService chatService;

    public Message sendMessage(SendMessageRequest request) {
        User user = userService.findById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getChatsMessages(Long chatId, User reqUser) {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)) {
            throw new AppException(ErrorCode.NOT_MEMBER);
        }

        List<Message> messageList = messageRepository.findByChatId(chatId);

        return messageList;
    }

    public Message findMessageById(Long messageId) {
        var optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            return optionalMessage.get();
        }
        throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
    }

    public void deleteMessage(Long messageId, User reqUser) {
        Message message = findMessageById(messageId);

        if (message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
        }

        throw new AppException(ErrorCode.CANT_DELETE);
    }
}
