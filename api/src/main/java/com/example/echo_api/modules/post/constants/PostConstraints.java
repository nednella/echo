package com.example.echo_api.modules.post.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class PostConstraints {

    public static final int NAME_MAX_LENGTH = 50;
    public static final int BIO_MAX_LENGTH = 160;
    public static final int LOCATION_MAX_LENGTH = 30;

}
