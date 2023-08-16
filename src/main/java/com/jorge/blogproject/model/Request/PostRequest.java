package com.jorge.blogproject.model.Request;

import java.util.List;

public record PostRequest(
        String title,
        String content,
        String user
){}
