// package com.me_social.MeSocial.controller.restController;

// import org.hibernate.mapping.List;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.me_social.MeSocial.entity.modal.Comment;

// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;

// @RestController
// @RequestMapping("/api/comments")
// @RequiredArgsConstructor
// @FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
// public class CommentRestController {

//     CommentService commentService;

//     @PostMapping
//     public Comment createComment(@RequestBody Comment comment) {
//         return commentService.save(comment);
//     }

//     @GetMapping
//     public List<Comment> getAllComments() {
//         return commentService.findAll();
//     }

//     // Các phương thức khác như update, delete
// }