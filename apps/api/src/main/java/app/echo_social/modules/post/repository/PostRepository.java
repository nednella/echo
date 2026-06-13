package app.echo_social.modules.post.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import app.echo_social.modules.post.entity.Post;

@Repository
public interface PostRepository
    extends CrudRepository<Post, UUID>, PagingAndSortingRepository<Post, UUID>, CustomPostRepository {}
