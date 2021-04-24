package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieEntry;
import com.myapp.gradutest_android.adapter.PieChartAdapter;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Data_Analyse_Activity extends AppCompatActivity {

    private PieChart credit_chart;
    private List<Integer> colors = new ArrayList<>();
    private List<PieEntry> vals = new ArrayList<>();
    private PieChartAdapter pieChartAdapter;
    private ImageView back_btn;
    private ImageView more_info_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__analyse);
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Data_Analyse_Activity.this,"更多信息","\n\n暂时仅支持查看过去一个月的数据\n").show();
            }
        });

        //刷新积分图表
        MMKV mmkv = MMKV.defaultMMKV();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").encodedAuthority(getString(R.string.host_core))
                .appendPath("getCreditAnalyse")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        String url = builder.build().toString();
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(refreshCreditChartHandler,url,1));
        t.start();
    }

    public void initView(){
        credit_chart = findViewById(R.id.credit_chart);
        back_btn = findViewById(R.id.img_back_data_analyse);
        more_info_btn = findViewById(R.id.img_more_info_data_analyse);
        initCreditChart();
    }

    public void initCreditChart() {
        vals.add(new PieEntry(1.0f, "签到收入"));
        vals.add(new PieEntry(1.0f, "好习惯打卡收入"));
        vals.add(new PieEntry(1.0f, "好习惯购买支出"));
        vals.add(new PieEntry(1.0f, "积分兑换支出"));
        vals.add(new PieEntry(1.0f, "积分抽奖支出"));

        // 设置每份的颜色
        colors.add(Color.parseColor("#6785f2"));
        colors.add(Color.parseColor("#675cf2"));
        colors.add(Color.parseColor("#FF8247"));
        colors.add(Color.parseColor("#FF6A6A"));
        colors.add(Color.parseColor("#B22222"));
        pieChartAdapter = new PieChartAdapter(credit_chart);
        pieChartAdapter.initData(vals, colors, "积分图表", 1, 3f);
        pieChartAdapter.initPercent(true, 12, Color.GRAY, 0.8f, 0.8f, 80f, Color.GRAY);
        //样式设置

        // 中心圆中是否有字(具体值具体写入)
        pieChartAdapter.initCenterText(true, "积分使用", Color.GRAY, 32);
        // 是否可以拖拽旋转(具体值具体写入)
        pieChartAdapter.initRotation(true, 0, 0f, false);
        // 是否有描述(具体值具体写入)
        pieChartAdapter.initDescription(true, "过去一个月数据", 14, Color.BLACK, 300f, 100f);
        // 是否显示每个部分的文字描述(具体值具体写入)
        pieChartAdapter.initEntryLabel(true, Color.WHITE, 7);
        // 图表位置及背景(具体值具体写入)
        pieChartAdapter.initExtraOffset(50, 20, 50, 5, Color.WHITE);
        // 获取图例(具体值具体写入)
        pieChartAdapter.initLegend(false, Legend.LegendOrientation.VERTICAL,
                Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT,
                -90f, 0f, Color.WHITE, 14);
        pieChartAdapter.invalidate();
    }


    @SuppressLint("HandlerLeak")
    Handler refreshCreditChartHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","refreshCreditChartHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONObject jsonObject = new JSONObject(val);
                if(jsonObject.getInt("code") == 0) {
                    vals.clear();
                    vals.add(new PieEntry(jsonObject.getInt("data1"), "签到收入"));
                    vals.add(new PieEntry(jsonObject.getInt("data2"), "好习惯打卡收入"));
                    vals.add(new PieEntry(jsonObject.getInt("data3"), "好习惯购买支出"));
                    vals.add(new PieEntry(jsonObject.getInt("data4"), "积分兑换支出"));
                    vals.add(new PieEntry(jsonObject.getInt("data5"), "积分抽奖支出"));
                    pieChartAdapter.invalidate();
                }else {
                    miniToast.Toast(Data_Analyse_Activity.this,jsonObject.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}