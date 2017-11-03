package cn.edu.zju.accountbook.accountbookdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by 张昊 on 2017/10/31.
 */

public class Data {

    Context context;
    private static MyDatabaseHelper dbHelper ;
    private static SQLiteDatabase db ;

    public Data(Context context) {

        dbHelper = new MyDatabaseHelper(context,"AccountBook.db",null,1);
        db = dbHelper.getWritableDatabase();
    }


    public void setContext(Context context) {
        this.context = context;
    }





    /***
     * 建表
     */




    /**
     * 数据结构及其获取方法  张昊
     */

    private static int ID;
    private static String usage;
    private static double price;
    private static String time;
    private static int locationValid;
    private static String locationDescribe;
    private static String address;
    private static String city;
    private static String district;
    private static String street;
    private static int photo_valid;
    private static String photo;
    private static String other1;
    private static String other2;

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        Data.ID = ID;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLocationValid() {
        return locationValid;
    }

    public void setLocationValid(int locationValid) {
        this.locationValid = locationValid;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPhoto_valid() {
        return photo_valid;
    }

    public void setPhoto_valid(int photo_valid) {
        this.photo_valid = photo_valid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }




    /***
     * 增删改查
     */
    public int insert()
    {
        ContentValues values = new ContentValues();
        values.put("usage",usage);
        values.put("price",price);
        values.put("time",time);
        values.put("location_Valid",locationValid);
        values.put("location_describe",locationDescribe);
        values.put("address",address);
        values.put("city",city);
        values.put("district",district);
        values.put("street",street);
        values.put("photo_valid",photo_valid);
        values.put("photo",photo);
        values.put("other1",other1);
        values.put("other2",other2);
        db.insert("AccountBook",null,values);//插入第一条数据
        values.clear();

        return 0;
    }

    public int delete(){
        return 0;
    }

    public int update(){
        return 0;
    }

    public int select(){
        return 0;
    }

    public void printTable(){
        /***
         * 待添加  判断是否存在表
         */
        Cursor cursor = db.query("AccountBook",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            //遍历cursor对象，取出数据并打印
             do{
                 Log.v("1","1");
                 ID = cursor.getInt(cursor.getColumnIndex("usage"));
                 usage = cursor.getString(cursor.getColumnIndex("usage"));
                 price = cursor.getDouble(cursor.getColumnIndex("price"));
                 time = cursor.getString(cursor.getColumnIndex("time"));
                 locationValid = cursor.getInt(cursor.getColumnIndex("location_valid"));
                 locationDescribe = cursor.getString(cursor.getColumnIndex("location_describe"));
                 address = cursor.getString(cursor.getColumnIndex("address"));
                 city = cursor.getString(cursor.getColumnIndex("city"));
                 district = cursor.getString(cursor.getColumnIndex("district"));
                 street = cursor.getString(cursor.getColumnIndex("street"));
                 photo_valid = cursor.getInt(cursor.getColumnIndex("photo_valid"));
                 photo = cursor.getString(cursor.getColumnIndex("photo"));
                 other1 = cursor.getString(cursor.getColumnIndex("other1"));
                 other2 = cursor.getString(cursor.getColumnIndex("other2"));

                 Log.v("info","*" +ID+"*" +usage+"*"+price+"*" +time+"*" +locationValid+"*" +locationDescribe+"*" +
                         address+"*" +city+"*" +district+"*" +street+"*" +photo_valid+"*" +photo
                         +"*" +other1+"*"+other2);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}
