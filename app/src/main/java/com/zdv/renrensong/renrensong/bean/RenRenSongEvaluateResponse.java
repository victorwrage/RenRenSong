package com.zdv.renrensong.renrensong.bean;

import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;

import java.util.ArrayList;
/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class RenRenSongEvaluateResponse {
    String status;
    String info;
    ArrayList<RenRenSongEvaluateInfo> content;

    public ArrayList<RenRenSongEvaluateInfo> getContent() {
        return content;
    }

    public void setContent(ArrayList<RenRenSongEvaluateInfo> content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    @Override
    public String toString() {
        if(content==null){
            return status+info;
        }
        return status+info+content.size();
    }


}
