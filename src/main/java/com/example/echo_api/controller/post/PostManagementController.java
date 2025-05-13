package com.example.echo_api.controller.post;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.service.post.management.PostManagementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class PostManagementController {

    private final PostManagementService postManagementService;

}
