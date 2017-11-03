package com.zdv.renrensong.renrensong.view;

import com.zdv.renrensong.renrensong.RenRenSongContentInfo;

import java.util.List;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/6/8 11:33
 */

public interface IDBContentView extends IView {

      void ResolveDBContentData(List<RenRenSongContentInfo> info);
      void ResolveDBContentInsertData(boolean isInsert);
      void ResolveDBContentDeleteData(boolean isDelete);

}
