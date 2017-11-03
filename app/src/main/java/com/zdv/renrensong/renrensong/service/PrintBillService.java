package com.zdv.renrensong.renrensong.service;

import android.app.IntentService;
import android.content.Intent;
import android.device.PrinterManager;

import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.util.Utils;

public class PrintBillService extends IntentService {

    private PrinterManager printer;
    Utils util;
    public PrintBillService() {
        super("bill");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        KLog.v("onCreate");
        util = Utils.getInstance();
        printer = new PrinterManager();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String order_num = intent.getStringExtra("order_num");
        String order_id = intent.getStringExtra("order_id");
        String receiver_id = intent.getStringExtra("receiver_id");
        String g_time = intent.getStringExtra("g_time");
        String item_name = intent.getStringExtra("item_name");

        String qcode = intent.getStringExtra("QCODE");
        KLog.v("print onHandleIntent" + printer);

        printer.prn_setBlack(6);
        printer.setGrayLevel(4);
        printer.setSpeedLevel(3);
        printer.setupPage(384, -1);
        int ret = printer.drawTextEx("签收凭证", 100, 0, -1, -1, "arial", 45, 0, 0, 0);
        ret+=15;
        if (order_num != null && !order_num.equals("")) {
            ret += printer.drawTextEx("订单号："+order_num , 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        }
        if (order_id != null&& !order_id.equals("")) {
            ret += printer.drawTextEx("订单ID："+order_id , 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        }
        if (receiver_id != null&& !receiver_id.equals("")) {
            ret += printer.drawTextEx("配送员ID："+receiver_id , 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        }
        if (g_time != null&& !g_time.equals("")) {
            ret += printer.drawTextEx("派件时间："+g_time , 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        }
        if (item_name != null&& !item_name.equals("")) {
            ret += printer.drawTextEx("物品名称:"+item_name , 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        }

        ret += printer.drawTextEx("完成时间："+ util.currentDate("yyyy-MM-dd HH:mm:ss"), 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        ret+=10;
        ret += printer.drawLine(10, ret, 360, ret, 2);
        ret+=10;
        ret += printer.drawTextEx("备注：", 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        ret+=10;
        ret += printer.drawLine(10, ret, 360, ret, 2);
        ret+=10;
        ret += printer.drawTextEx("", 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        ret+=20;
        ret += printer.drawTextEx("签名：", 5, ret, -1, -1, "arial", 22, 0, 0, 0);
        ret+=70;
        ret += printer.drawTextEx("您的专属快递->人人送", 5, ret, -1, -1, "arial", 16, 0, 0, 0);
        ret += printer.drawTextEx("扫描下面的二维码评价我们的服务", 5, ret, -1, -1, "arial", 16, 0, 0, 0);
        ret+=10;
        ret += printer.drawBarcode(qcode, 70, ret, 58, 5, 800, 0);
        ret+=20;
        //  printer.prn_paperBack(50);
        // printer.drawLine(5,0,384,0,384);
        //   int  ret = printer.drawText("POS支付凭证",100,0,"arial",45,true,false,0);
        android.util.Log.i("debug", "ret:" + ret);
        // int  ret;
        /*printer.drawText("备注:",5,0,"arial",24,false,false,0);
        printer.drawText("您的24H商户贴身管家",5,0,"arial",24,false,false,0);
        printer.drawText("微信关注 百宝平台(baibao)",5,0,"arial",24,false,false,0);
        printer.drawText(" 中大威客服热线升级为：8008208820",5,0,"arial",24,false,false,0);
        printer.drawText("",5,0,"arial",24,false,false,0);
        printer.drawText("签名：:",200,0,"arial",24,false,false,0);*/


        //  android.util.Log.i("debug", "ret:" + ret);
        //sleep(5000);
        ret = printer.printPage(0);
        printer.prn_paperForWard(15);
        Intent i = new Intent("android.print.message");
        i.putExtra("ret", ret);
        this.sendBroadcast(i);
    }

    private void sleep(int second) {
        //延时1秒
        try {
            Thread.currentThread();
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}