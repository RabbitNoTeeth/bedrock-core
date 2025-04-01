package com.gitee.rabbitnoteeth.bedrock.core.test;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;

public class Application {

    public static void main(String[] args) {
        HttpMethod httpMethod = HttpMethod.valueOf("POST");
        System.out.println(httpMethod);
    }

}
