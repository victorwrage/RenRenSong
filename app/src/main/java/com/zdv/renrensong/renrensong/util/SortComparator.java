package com.zdv.renrensong.renrensong.util;

import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/27 9:37
 */

public class SortComparator implements Comparator<RenRenSongContentInfo> {
    private int type = 0;
    private int order = 0;

    public SortComparator(int type, int order) {
        this.type = type;
        this.order = order;
    }

    @Override
    public int compare(RenRenSongContentInfo o1, RenRenSongContentInfo o2) {
        switch (type) {
            case 0://距离
                switch (order) {
                    case 0://升序
                        if (o1.getDistance() != null && o2.getDistance() != null) {
                            if (Integer.parseInt(o1.getDistance()) >= Integer.parseInt(o2.getDistance())) {
                                return 1;
                            }
                        }
                    break;
                    case 1:
                        if (o1.getDistance() != null && o2.getDistance() != null) {
                            if (Integer.parseInt(o1.getDistance()) <= Integer.parseInt(o2.getDistance())) {
                                return 1;
                            }
                        }
                        break;
                }
                break;
            case 1://时间
                switch (order) {
                    case 0:
                        if (o1.getCreate_time() != null && o2.getCreate_time() != null) {
                            if (ValidateFormat(o1.getCreate_time()) >= ValidateFormat(o2.getCreate_time())) {
                                return 1;
                            }
                        }
                        break;
                    case 1:
                        if (o1.getCreate_time() != null && o2.getCreate_time() != null) {
                            if (ValidateFormat(o1.getCreate_time()) <= ValidateFormat(o2.getCreate_time())) {
                                return 1;
                            }
                        }
                        break;
                }
                break;
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    private long ValidateFormat(String date){

        long result = -1L;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            result = format.parse(date).getTime();
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            KLog.v("ValidateFormat error");
        }
        return result;
    }
}
