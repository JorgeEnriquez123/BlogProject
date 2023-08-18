package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.PostEntity;
import com.jorge.blogproject.model.Request.PostRequest;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.PostService;
import com.jorge.blogproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {
    PostService postService;
    UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(postService.findAll());
    }
    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(postService.findAllPaginable(page));
    }

    @GetMapping
    public ResponseEntity<?> findAllAvailablePosts(){
        return ResponseEntity.ok(postService.findPostsNotDeleted());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<PostEntity> postFound = postService.findById(id);
        if(postFound.isPresent()){
            return ResponseEntity.ok().body(postFound.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody PostRequest postRequest, BindingResult result){
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequest.title());
        postEntity.setContent(postRequest.content());

        UserEntity userFound = userService.findByUsername(postRequest.user());
        if(userFound == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        postEntity.setUser(userFound);
        return ResponseEntity.ok().body(postService.save(postEntity));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id){
        PostEntity postDeleted = postService.delete(id);
        if(postDeleted != null){
            return ResponseEntity.ok().body(postDeleted);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        PostEntity postRestored = postService.restore(id);
        if(postRestored != null){
            return ResponseEntity.ok().body(postRestored);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");
    }


}
