package com.example.echo_api.controller.profile;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.example.echo_api.exception.AbstractControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(assignableTypes = {
        ProfileViewController.class,
        ProfileInteractionController.class,
        ProfileManagementController.class })
public class ProfileControllerAdvice extends AbstractControllerAdvice {

}
