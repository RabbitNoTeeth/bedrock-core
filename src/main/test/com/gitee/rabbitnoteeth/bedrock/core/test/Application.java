package com.github.rabbitnoteeth.bedrock.core.test;

import com.github.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;

public class Application {

    public static void main(String[] args) {
        HttpMethod httpMethod = HttpMethod.valueOf("POST");
        System.out.println(httpMethod);
    }

}
