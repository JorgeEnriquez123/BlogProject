package com.jorge.blogproject.service;

import com.jorge.blogproject.model.PostEntity;
import com.jorge.blogproject.repository.PostRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements GenericDao<PostEntity> {

    final private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostEntity> findAll() {
        return postRepository.findAll();
    }

    public List<PostEntity> findPostsNotDeleted(){
        return postRepository.findPostsNotDeleted();
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return postRepository.save(postEntity);
    }

    @Override
    public Optional<PostEntity> findById(Long id) {
        return postRepository.findById(id);
    }

    public void delete(PostEntity postEntity) {
        postEntity.setDeletedAt(LocalDateTime.now());
        postRepository.save(postEntity);
    }

    public void restore(PostEntity postEntity){
        postEntity.setDeletedAt(null);
        postRepository.save(postEntity);
    }

}
