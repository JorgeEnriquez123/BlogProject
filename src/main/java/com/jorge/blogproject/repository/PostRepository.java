package com.jorge.blogproject.repository;

import com.jorge.blogproject.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p WHERE p.deletedAt IS NULL")
    List<PostEntity> findPostsNotDeleted();
}
