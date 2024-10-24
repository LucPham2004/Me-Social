package com.me_social.MeSocial.entity.modal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Media {
    @Id
    private String publicId;
    
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference(value = "post_medias")
    private Post post;
}
