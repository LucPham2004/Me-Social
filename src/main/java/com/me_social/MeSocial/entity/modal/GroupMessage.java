 package com.me_social.MeSocial.entity.modal;

 import java.time.LocalDateTime;

 import jakarta.persistence.Column;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
 import jakarta.persistence.Id;
 import lombok.AllArgsConstructor;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;

 @Entity
 @AllArgsConstructor
 @NoArgsConstructor
 @Getter
 @Setter
 public class GroupMessage {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "created_at", nullable = false, updatable = false)
     private LocalDateTime createdAt;

 }
