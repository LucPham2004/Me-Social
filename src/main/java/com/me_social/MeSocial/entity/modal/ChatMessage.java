package com.me_social.MeSocial.entity.modal;

import com.me_social.MeSocial.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ChatMessage { 
   private MessageType type; 
   private String content; 
   private Long senderId;
   private Long receiverId;
   private String url;
}
