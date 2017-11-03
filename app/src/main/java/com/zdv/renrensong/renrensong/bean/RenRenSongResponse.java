package com.zdv.renrensong.renrensong.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class RenRenSongResponse {
    String status;
    String info;
    Info content;

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

    public Info getContent() {
        return content;
    }

    public void setContent(Info content) {
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

    public class Info {
        String userID;
        String TOKEN;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getTOKEN() {
            return TOKEN;
        }

        public void setTOKEN(String TOKEN) {
            this.TOKEN = TOKEN;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getDeliverer_id() {
            return deliverer_id;
        }

        public void setDeliverer_id(String deliverer_id) {
            this.deliverer_id = deliverer_id;
        }

        String user_name;
        String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }


        String deliverer_id;


        @Override
        public String toString() {
            return userID+TOKEN+user_name+deliverer_id;
        }
    }
}
