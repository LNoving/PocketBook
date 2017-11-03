package cn.edu.zju.accountbook.accountbookdemo;

/**
 * Created by 张昊 on 2017/10/31.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 张昊 on 2017/10/9.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    /***
     * 创建表
     */
    public static final String CREATE_ACCOUNTBOOK = "CREATE TABLE AccountBook("
            +"ID INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +"usage text,"             //用途
            +"price real,"             //价格
            +"time text,"              //时间
            +"location_valid integer,"  //是否存有地址
            +"location_describe text," //位置语义化描述
            +"address text,"           //地址
            +"city text,"              //城市
            +"district text,"          //区
            +"street text,"             //街道
            +"photo_valid integer,"      //照片是否存在
            +"photo text,"              //照片URL
            +"other1 text,"             //其他
            +"other2 text)";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_ACCOUNTBOOK);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }


    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {

    }
}