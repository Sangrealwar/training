package spittr.web.util;

import java.io.Serializable;

/**
 * 该实体类用于封装返回的JSON数据类型
 *		{
 “status”：状态，
 “msg"：消息，
 “repository”：数据
 * Created by macro_su on 2017/1/10.
 */
public class Result implements Serializable {
    private int status;//状态：约定0表示正确；其他表示错误
    private String msg;//消息
    private Object data;//传出去的数据
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
