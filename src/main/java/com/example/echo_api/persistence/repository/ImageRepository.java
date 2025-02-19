package com.example.echo_api.persistence.repository;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import com.example.echo_api.persistence.model.image.Image;

@Repository
public interface ImageRepository extends ListCrudRepository<Image, UUID> {

}
