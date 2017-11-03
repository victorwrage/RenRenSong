package com.zdv.renrensong.renrensong.bean;

import com.zdv.renrensong.renrensong.RenRenSongContentInfo;

import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class RenRenSongContentResponse {
    String status;
    String info;
    ArrayList<RenRenSongContentInfo> content;

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

    public ArrayList<RenRenSongContentInfo> getContent() {
        return content;
    }

    public void setContent(ArrayList<RenRenSongContentInfo> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        if(content==null) {
            return status + info ;
        }else{
            return status + info + content.toString();
        }
    }


}
