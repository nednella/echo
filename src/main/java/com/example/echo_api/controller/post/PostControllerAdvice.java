package com.example.echo_api.controller.post;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.example.echo_api.exception.AbstractControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(assignableTypes = {
        PostViewController.class,
        PostInteractionController.class,
        PostManagementController.class })
public class PostControllerAdvice extends AbstractControllerAdvice {

}
