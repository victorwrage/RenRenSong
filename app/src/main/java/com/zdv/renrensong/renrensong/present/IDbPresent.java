package com.zdv.renrensong.renrensong.present;


import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:46
 */

public interface IDbPresent {
    void QueryEvaluate();
    void InsertReplaceEvaluate(RenRenSongEvaluateInfo item);
    void DeleteEvaluate(RenRenSongEvaluateInfo item);
    void QueryContent();
    void InsertReplaceContent(RenRenSongContentInfo item);
    void DeleteContent(RenRenSongContentInfo item);
}
