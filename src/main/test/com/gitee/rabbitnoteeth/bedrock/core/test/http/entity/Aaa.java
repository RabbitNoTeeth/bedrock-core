package com.gitee.rabbitnoteeth.bedrock.core.test.http.entity;

import com.github.rabbitnoteeth.bedrock.util.validation.annotation.Validate;
import com.github.rabbitnoteeth.bedrock.util.validation.entity.Rule;

import java.util.List;

public class Aaa {

    private String aaa;

    private List<String> bbb;

    @Validate(rule = Rule.NOT_BLANK, message = "abc不能为空")
    private String abc;

    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    public List<String> getBbb() {
        return bbb;
    }

    public void setBbb(List<String> bbb) {
        this.bbb = bbb;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    @Override
    public String toString() {
        return "Aaa{" +
            "aaa='" + aaa + '\'' +
            ", bbb='" + bbb + '\'' +
            ", abc='" + abc + '\'' +
            '}';
    }
}
