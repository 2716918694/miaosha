package com.miaoshaproject.response;

public class CommonReturnType {
    //表明对应请求返回的处理结果 "success"或"fail"
    private String Status;
    //若status为success，则data返回前端对应的json数据
    //若status为fail。则data内使用通用的错误码格式
    private Object data;

    //定义通用创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
