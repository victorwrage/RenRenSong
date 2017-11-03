package com.zdv.renrensong.renrensong.view;

import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;

import java.util.List;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/6/8 11:33
 */

public interface IDBEvaluateView extends IView {

      void ResolveDBEvaluateData(List<RenRenSongEvaluateInfo> info);
      void ResolveDBEvaluateInsertData(boolean isInsert);
      void ResolveDBEvaluateDeleteData(boolean isDelete);

}
