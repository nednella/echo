package com.example.echo_api.controller.post;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.service.post.view.PostViewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class PostViewController {

    private final PostViewService postViewService;

}
