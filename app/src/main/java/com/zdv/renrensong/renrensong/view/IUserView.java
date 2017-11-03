package com.zdv.renrensong.renrensong.view;


import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;

import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IUserView extends IView{
    /**
     * @param info
     */
    void ResolveLoginInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveRegisterInfo(RenRenSongCodeResponse info);

    /**
     * @param info
     */
    void ResolveQcodeInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveCodeInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolveForgetInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolveAlterInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolveEmailInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolveVerifyInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolveCodeVerifyInfo(RenRenSongCodeResponse info);
    /**
     * @param info
     */
    void ResolvePhoneChangeInfo(RenRenSongCodeResponse info);
}
