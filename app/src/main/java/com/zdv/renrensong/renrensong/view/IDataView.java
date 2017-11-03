package com.zdv.renrensong.renrensong.view;


import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IDataView extends IView{
    /**
     * @param info
     */
    void ResolveFetchInfo(RenRenSongContentResponse info);
    void ResolveOrderInfo(RenRenSongCodeResponse info);

}
