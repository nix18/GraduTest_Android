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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.myapp.gradutest_android.adapter.LineChartAdapter;
import com.myapp.gradutest_android.adapter.PieChartAdapter;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Data_Analyse_Activity extends AppCompatActivity {

    private PieChart credit_chart;
    private LineChart clock_in_chart;
    private List<Integer> colors = new ArrayList<>();
    private List<PieEntry> vals = new ArrayList<>();
    private ArrayList<Entry> values = new ArrayList<>();
    private PieChartAdapter pieChartAdapter;
    private LineChartAdapter lineChartAdapter;
    private ImageView back_btn;
    private ImageView more_info_btn;
    private String[] xStr;

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

        //刷新打卡图表
        Uri.Builder builder1 = new Uri.Builder();
        builder1.scheme("https").encodedAuthority(getString(R.string.host_core))
                .appendPath("getHabitClockInAnalyse")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        String url1 = builder1.build().toString();
        networkTask networkTask1=new networkTask();
        Thread t1=new Thread(networkTask1.setParam(refreshClockInChartHandler,url1,1));
        t1.start();
    }

    public void initView(){
        credit_chart = findViewById(R.id.credit_chart);
        clock_in_chart = findViewById(R.id.clock_in_chart);
        back_btn = findViewById(R.id.img_back_data_analyse);
        more_info_btn = findViewById(R.id.img_more_info_data_analyse);
        initCreditChart();
        initClockInChart();
    }

    public void initCreditChart() {
        vals.add(new PieEntry(1.0f, "签到收入"));
        vals.add(new PieEntry(1.0f, "好习惯打卡收入"));
        vals.add(new PieEntry(1.0f, "好习惯购买支出"));
        vals.add(new PieEntry(1.0f, "积分兑换支出"));
        vals.add(new PieEntry(1.0f, "积分抽奖支出"));

        // 设置每份的颜色
        colors.add(Color.parseColor("#6785F2"));
        colors.add(Color.parseColor("#675CF2"));
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
        pieChartAdapter.initDescription(true, "过去30天数据", 14, Color.BLACK, 300f, 100f);
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

    public void initClockInChart(){
        xStr = new String[]{"12/1", "12/2", "12/3"};
        lineChartAdapter = new LineChartAdapter(clock_in_chart);
        lineChartAdapter.setXAxis(true, true, 10f, Color.BLUE, XAxis.XAxisPosition.BOTTOM, 10f,3,xStr);


        lineChartAdapter.setData();
        lineChartAdapter.setInteraction(true,true, true, true, false,
                false, false, true);
        lineChartAdapter.invalidate();
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
                    if(jsonObject.getInt("data1")!=0)
                        vals.add(new PieEntry(jsonObject.getInt("data1"), "签到收入"));
                    if(jsonObject.getInt("data2")!=0)
                        vals.add(new PieEntry(jsonObject.getInt("data2"), "好习惯打卡收入"));
                    if(jsonObject.getInt("data3")!=0)
                        vals.add(new PieEntry(jsonObject.getInt("data3"), "好习惯购买支出"));
                    if(jsonObject.getInt("data4")!=0)
                        vals.add(new PieEntry(jsonObject.getInt("data4"), "积分兑换支出"));
                    if(jsonObject.getInt("data5")!=0)
                        vals.add(new PieEntry(jsonObject.getInt("data5"), "积分抽奖支出"));
                    if(jsonObject.getInt("data1")==0&&jsonObject.getInt("data2")==0&&jsonObject.getInt("data3")==0&&
                            jsonObject.getInt("data4")==0&&jsonObject.getInt("data5")==0)
                        miniToast.Toast(Data_Analyse_Activity.this,"暂无数据，请稍候再来");
                    pieChartAdapter.invalidate();
                }else {
                    miniToast.Toast(Data_Analyse_Activity.this,jsonObject.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler refreshClockInChartHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","refreshCreditChartHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONObject jsonObject = new JSONObject(val);
                if(jsonObject.getInt("code") == 0) {
                    JSONObject json = jsonObject.getJSONObject("result");
                    ArrayList<String> str = new ArrayList<>();
                    values.clear();
                    Iterator iterator = json.keys();
                    int i = 1;
                    //枚举Json，赋值图表数据
                    while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        Integer value = json.getInt(key);
                        str.add(key);
                        values.add(new Entry(i,value));
                        i++;
                    }
                    xStr = (String[]) str.toArray(new String[0]);
                    lineChartAdapter.addData(values, "每日打卡次数", 0f, Color.BLUE,8f,
                            false, 5f, Color.GREEN,
                            true, Color.YELLOW);
                    lineChartAdapter.mLineChart.zoom(0, 1f, 0, 0);
                    lineChartAdapter.mLineChart.zoom(8f, 1f, 0, 0);
                    lineChartAdapter.setData();
                    lineChartAdapter.setXAxis(true, true, 10f, Color.BLUE, XAxis.XAxisPosition.BOTTOM, 10f,xStr.length,xStr);
                    lineChartAdapter.invalidate();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}