package com.me_social.MeSocial.service;

import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.modal.Tag;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.TagRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class TagService {
    TagRepository tagRepository;
    
    public Tag createTag(String nameTag) {
        Tag tag = new Tag();
        tag.setName(nameTag);
        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        if(tagRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        tagRepository.delete(tagRepository.findById(id).get());
    }

    public void deleteTag(Tag tag) {
        if(tagRepository.existsById(tag.getId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        tagRepository.delete(tag);
    }
}
