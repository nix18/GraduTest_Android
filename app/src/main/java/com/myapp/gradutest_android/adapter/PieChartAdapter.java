package com.myapp.gradutest_android.adapter;

import android.graphics.Typeface;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.List;
/**
 * 饼状图表
 */
public class PieChartAdapter {
    public PieChart mPieChart;
    private PieDataSet pieDataSet;
    public PieChartAdapter(PieChart pieChart) {
        this.mPieChart = pieChart;
    }

    /**
     * 饼状图中间是否显示中间的洞
     * @param isHole 是否显示中间的洞
     * @param vHole 设置中间洞的大小
     * @param vCircleRadius 半径
     * @param vCircleColor 颜色
     * @param vCircleAlpha 透明度
     * */
    public void initCenter(boolean isHole, float vHole, float vCircleRadius, int vCircleColor, int vCircleAlpha) {
        mPieChart.setDrawHoleEnabled(isHole);// 是否显示中间的洞
        mPieChart.setHoleRadius(vHole);// 设置中间洞的大小
        // 半透明圈
        mPieChart.setTransparentCircleRadius(vCircleRadius);
        mPieChart.setTransparentCircleColor(vCircleColor); //设置半透明圆圈的颜色
        mPieChart.setTransparentCircleAlpha(vCircleAlpha); //设置半透明圆圈的透明度
    }
    /**
     * 饼状图中间是否添加文字
     * @param isTxt 是否添加文字
     * @param txt 设置中间文字
     * @param txtColor 中间文字的颜色
     * @param txtSize 中间文字的大小px
     * */
    public void initCenterText(boolean isTxt, String txt, int txtColor, int txtSize) {
        mPieChart.setDrawCenterText(isTxt); //是否添加文字
        if (isTxt){
            mPieChart.setCenterText(txt); //设置中间文字
            mPieChart.setCenterTextColor(txtColor); //中间文字的颜色
            mPieChart.setCenterTextSizePixels(txtSize);  //中间文字的大小px
            mPieChart.setCenterTextRadiusPercent(1f);
            mPieChart.setCenterTextTypeface(Typeface.DEFAULT); //中间文字的样式
            mPieChart.setCenterTextOffset(0, 0); //中间文字的偏移量
        }
    }
    /**
     * 是否可以手动旋转
     * @param isRation 是否可以手动旋转
     * @param rotationAngle 初始旋转角度
     * @param dfc 设置pieChart图表转动阻力摩擦系数[0,1]
     * @param isPercent 是否显示成百分比
     * */
    public void initRotation(boolean isRation, int rotationAngle, float dfc, boolean isPercent) {
        mPieChart.setRotationEnabled(isRation);// 是否可以手动旋转
        if (isRation){
            mPieChart.setRotationAngle(rotationAngle);// 初始旋转角度
            mPieChart.setDragDecelerationFrictionCoef(dfc);//设置pieChart图表转动阻力摩擦系数[0,1]
        }
        mPieChart.setUsePercentValues(isPercent);//是否显示成百分比
    }
    /**
     * 是否取消右下角描述
     * @param isDescription 是否取消右下角描述
     * */
    public void initDescription(boolean isDescription, String des, int size, int color, float x, float y){//Paint.Align align) {
        mPieChart.getDescription().setEnabled(isDescription); //是否取消右下角描述
        if (isDescription){
            Description description = new Description();
            description.setText(des);
            description.setTextSize(size);
            description.setTextColor(color);
            //description.setTextAlign(align);
            description.setPosition(x, y);
            mPieChart.setDescription(description);
        }
    }
    /**
     * 是否显示每个部分的文字描述
     * @param isDraw 是否显示每个部分的文字描述
     * @param etColor 描述文字的颜色
     * @param etSize 描述文字的大小
     * */
    public void initEntryLabel(boolean isDraw, int etColor, int etSize) {
        mPieChart.setDrawEntryLabels(isDraw);//是否显示每个部分的文字描述
        if (isDraw){
            mPieChart.setEntryLabelColor(etColor); //描述文字的颜色
            mPieChart.setEntryLabelTextSize(etSize);//描述文字的大小
            mPieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD); //描述文字的样式
        }
    }
    /**
     * 图相相关
     * @param left 图相对于上下左右的偏移
     * @param top 图相对于上下左右的偏移
     * @param right 图相对于上下左右的偏移
     * @param bom 图相对于上下左右的偏移
     * @param color 图标的背景色
     * */
    public void initExtraOffset(int left, int top, int right, int bom, int color) {
        mPieChart.setExtraOffsets(left, top, right, bom);//图相对于上下左右的偏移
        mPieChart.setBackgroundColor(color);//图标的背景色
    }
    /**
     * 获取图例
     * @param isEnacled 是否启用图列
     * @param orientation 设置图例水平显示: HORIZONTAL / VERTICAL
     * @param vAlignment 垂直布局：TOP/CENTER/BOTTOM
     * @param hAlignment 水平布局：RIGHT/CENTER/LEFT
     * @param xOs x轴的偏移
     * @param yOs y轴的偏移
     * @param color 字颜色
     * @param size 字大小
     * */
    public void initLegend(boolean isEnacled,
                           Legend.LegendOrientation orientation,
                           Legend.LegendVerticalAlignment vAlignment,
                           Legend.LegendHorizontalAlignment hAlignment, float xOs, float yOs, int color, int size) {
        //获取图例
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(isEnacled);
        if (isEnacled){
            legend.setOrientation(orientation);        //设置图例水平显示
            legend.setVerticalAlignment(vAlignment);   //顶部
            legend.setHorizontalAlignment(hAlignment); //右对其
            legend.setForm(Legend.LegendForm.CIRCLE); //设置图例的形状
            legend.setFormToTextSpace(10f);			   //设置每个图例实体中标签和形状之间的间距
            legend.setYOffset(yOs);					   //设置比例块 Y 轴偏移量
            legend.setXOffset(xOs);                    //设置比例块 X 轴偏移量
            legend.setTextColor(color);                //图例文字的颜色
            legend.setTextSize(size);                  //设置图例标签文本的大小
        }
    }
    /**
     * 创建数据
     * @param pieEntryList 数据
     * @param colors 颜色
     * @param title 标题
     * @param sliceSpace 设置饼状Item之间的间隙
     * @param selSpace 设置饼状Item被选中时变化的距离
     * */
    public void initData(List<PieEntry> pieEntryList, List<Integer> colors, String title, int sliceSpace, float selSpace){
        //饼状图数据集 PieDataSet
        pieDataSet = new PieDataSet(pieEntryList, title);
        pieDataSet.setSliceSpace(sliceSpace);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(selSpace);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
    }
    /**
     * 设置数据
     * @param pieEntryList 数据
     * */
    public void setData(List<PieEntry> pieEntryList) {
        pieDataSet = (PieDataSet) mPieChart.getData().getDataSetByIndex(0);
        pieDataSet.setValues(pieEntryList);
        mPieChart.getData().notifyDataChanged();
    }
    /**
     * 创建数据
     * @param isValueShow 设置是否显示数据实体(百分比，true:以下属性才有意义)
     * @param valueSize 设置所有DataSet内数据实体（百分比）的文本字体大小
     * @param valueColor 设置所有DataSet内数据实体（百分比）的文本颜色
     * @param part1 当值位置为外边线时，表示线的前半段长度
     * @param part2 当值位置为外边线时，表示线的后半段长度
     * @param per 当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
     * @param lineColor 当值位置为外边线时，表示线的颜色。
     * */
    public void initPercent(boolean isValueShow, int valueSize, int valueColor, float part1, float part2, float per, int lineColor){
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(isValueShow);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueTextColor(valueColor);  //设置所有DataSet内数据实体（百分比）的文本颜色
        pieData.setValueTextSize(valueSize);          //设置所有DataSet内数据实体（百分比）的文本字体大小
        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式

        pieDataSet.setValueLinePart1Length(part1);// 当值位置为外边线时，表示线的前半段长度。
        pieDataSet.setValueLinePart2Length(part2);// 当值位置为外边线时，表示线的后半段长度。
        pieDataSet.setValueLinePart1OffsetPercentage(per);// 当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        pieDataSet.setValueLineColor(lineColor);// 当值位置为外边线时，表示线的颜色。
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);// 设置Y值的位置是在圆内还是圆外

        mPieChart.setData(pieData);
    }

    /**
     * 是否显示百分比
     * */
    public void changePercent(boolean b){
        for (IDataSet set : mPieChart.getData().getDataSets())
        {
            set.setDrawValues(b);
        }
        this.invalidate();
    }
    /**
     * 刷新
     * */
    public void invalidate(){
        mPieChart.invalidate();
    }
    /**
     * 其他设置
     * */
    public void initOther(){
        mPieChart.highlightValues(null);
        mPieChart.setDrawSliceText(true);//是否隐藏饼图上文字
    }
}
