package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.PostEntity;
import com.jorge.blogproject.model.Request.PostRequest;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.PostService;
import com.jorge.blogproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> save(@RequestBody PostRequest postRequest){
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
        Optional<PostEntity> postFound = postService.findById(id);
        if(postFound.isPresent()){
            postService.delete(postFound.get());
            return ResponseEntity.ok().body("Post eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        Optional<PostEntity> postFound = postService.findById(id);
        if(postFound.isPresent()){
            postService.restore(postFound.get());
            return ResponseEntity.ok().body("Post restaurado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");
    }


}
