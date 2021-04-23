package com.myapp.gradutest_android.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import lombok.Data;

@Data
public class Goods {
    private Integer gid;
    private String goods_name;
    private String goods_pic_b64;
    private Integer goods_price;
    private Integer goods_stock;

    public Goods() {
    }

    public Goods(Integer gid, String goods_name, String goods_pic_b64, Integer goods_price, Integer goods_stock) {
        this.gid = gid;
        this.goods_name = goods_name;
        this.goods_pic_b64 = goods_pic_b64;
        this.goods_price = goods_price;
        this.goods_stock = goods_stock;
    }

    public Bitmap getGoodsPic() {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(goods_pic_b64.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
