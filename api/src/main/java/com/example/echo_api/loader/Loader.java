package com.example.echo_api.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class Loader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
    }

}
