package com.jorge.blogproject.service;

import com.jorge.blogproject.model.PostEntity;
import com.jorge.blogproject.repository.PostRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<PostEntity> findAllPaginable(Pageable pageable) {
        return postRepository.findAll(pageable);
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

    public PostEntity delete(Long id) {
        PostEntity postFound = postRepository.findById(id).orElse(null);
        if(postFound != null){
            postFound.setDeletedAt(LocalDateTime.now());
            postFound.setEnabled(false);
            return postRepository.save(postFound);
        }
        return postFound;
    }

    public PostEntity restore(Long id){
        PostEntity postFound = postRepository.findById(id).orElse(null);
        if(postFound != null){
            postFound.setDeletedAt(null);
            postFound.setEnabled(true);
            return postRepository.save(postFound);
        }
        return postFound;
    }

}
