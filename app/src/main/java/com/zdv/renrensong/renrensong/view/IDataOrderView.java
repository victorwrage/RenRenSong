package com.zdv.renrensong.renrensong.view;


import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;

/**
 * Info: 处理预约快递
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IDataOrderView extends IView{
    /**
     * @param info
     */
    void ResolveApointDateInfo(RenRenSongContentResponse info);
    /**
     * @param info
     */
    void ResolveCancelOrderInfo(RenRenSongCodeResponse info);

    /**
     * @param info
     */
    void ResolveOrderOrderInfo(RenRenSongCodeResponse info);

}
