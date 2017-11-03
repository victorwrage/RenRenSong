package com.zdv.renrensong.renrensong.present;


/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:46
 */

public interface IRequestPresent {
    void QueryRegister(String mobile_no,String code);
    void QueryCode(String mobile_no);
    void QueryQcode(String mobile);
    void QueryLogin(String mobile_no, String pw,String code,String session);
    void QueryForget(String mobile,  String code,  String user_pass);
    void QueryAlter(String useID, String TOKEN, String old_password, String new_password);
    void QueryEmail(String useID, String TOKEN, String email);
    void QueryVerify(String useID, String TOKEN,String real_name, String id_card_num);
    void QueryCodeVerify(String mobile, String code);
    void QueryPhoneChange(String mobile,String code,String useID, String TOKEN);
    void QueryFetch(String userID, String TOKEN, String sender_id,int start, int  count);
    void QueryFetchOrderOrder( String userID,String TOKEN,String order_id, String deliverer);

    void QueryFetchCancelOrder( String userID,String TOKEN,String order_id, String deliverer);
    void QueryFetchFetchOrder( String userID,String TOKEN, String order_id,String order_num,String receiver_id);
    void QueryApointData( String userID,String TOKEN, String deliverer);
    void QueryReceiveOrder( String userID,String TOKEN, String order_id,String order_num,String receiver_id);
    void QuerySend( String userID,String TOKEN,  String sender_id);
    void QueryPoi(String userID,String TOKEN,String location,String mobile,String deliverer_id);
    void QueryEvaluate(String userID,  String TOKEN,  String deliverer_id);



}
