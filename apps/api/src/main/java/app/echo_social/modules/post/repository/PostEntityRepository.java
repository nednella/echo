package app.echo_social.modules.post.repository;

import org.springframework.data.repository.ListCrudRepository;

import app.echo_social.modules.post.entity.PostEntity;
import app.echo_social.modules.post.entity.PostEntityPK;

public interface PostEntityRepository extends ListCrudRepository<PostEntity, PostEntityPK> {}
