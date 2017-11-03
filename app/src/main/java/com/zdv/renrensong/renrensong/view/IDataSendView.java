package com.zdv.renrensong.renrensong.view;


import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongSendResponse;

/**
 * Info: 处理预约快递
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IDataSendView extends IView{
    /**
     * @param info
     */
    void ResolveSendDateInfo(RenRenSongContentResponse info);

    /**
     * @param info
     */
    void ResolveReceiveOrderInfo(RenRenSongSendResponse info);

}
