package com.me_social.MeSocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.DirectMessageRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class DirectMessageService {

    DirectMessageRepository directMessageRepository;
    UserRepository userRepository;

    static int MESSAGES_PER_PAGE = 20;

    // Get messages
    public Page<DirectMessage> getUserDirectMessages(Long senderId, Long receiverId, int pageNum) {
        if(!userRepository.existsById(senderId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE);

        return directMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
    }

    // Send Message
    public DirectMessage saveDirectMessage(DirectMessage message) {
        return directMessageRepository.save(message);
    }

    // Delete Message
    public void deleteDirectMessage(Long messageId) {
        if(!directMessageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        directMessageRepository.delete(directMessageRepository.findById(messageId));
    }
}
