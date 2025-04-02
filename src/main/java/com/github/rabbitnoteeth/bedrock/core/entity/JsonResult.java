package com.github.rabbitnoteeth.bedrock.core.entity;

import com.github.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.github.rabbitnoteeth.bedrock.util.JsonUtils;

public class JsonResult {

  private Boolean success;
  private String message;
  private Object data;
  private int code;

  private JsonResult() {
  }

  public static JsonResult success(String message) {
    JsonResult res = new JsonResult();
    res.setSuccess(true);
    res.setMessage(message);
    res.setCode(HttpConstants.RESPONSE_STATUS_200);
    return res;
  }

  public static JsonResult success(String message, Object data) {
    JsonResult res = new JsonResult();
    res.setSuccess(true);
    res.setMessage(message);
    res.setData(data);
    res.setCode(HttpConstants.RESPONSE_STATUS_200);
    return res;
  }

  public static JsonResult fail(String message, int code) {
    JsonResult res = new JsonResult();
    res.setSuccess(false);
    res.setMessage(message);
    res.setCode(code);
    return res;
  }

  public static JsonResult fail(String message, int code, Object data) {
    JsonResult res = new JsonResult();
    res.setSuccess(false);
    res.setMessage(message);
    res.setData(data);
    res.setCode(code);
    return res;
  }

  public Boolean getSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public JsonResult setMessage(String message) {
    this.message = message;
    return this;
  }

  public Object getData() {
    return data;
  }

  public JsonResult setData(Object data) {
    this.data = data;
    return this;
  }

  public JsonResult setSuccess(Boolean success) {
    this.success = success;
    return this;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String encode() {
    return JsonUtils.encode(this);
  }

}
