package cn.edu.zju.accountbook.mypocketbook.cons;

/**
 * Created by 张昊 on 2017/12/10.
 *整个应用通用的常量
 *<br><b>类描述:</b>
 *<pre>|</pre>
 *@see
 *@since
 */

public class CommonConstants {

    public final static int REQUEST_CODE_ACCESS = 1;

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int BARCODE_REQUEST_CODE = 3;

    public static final int INCOME = 1;
    public static final int EXPENDITURE = 0;

    public static final int OTHER = 0;
    public static final int SHOPPING = 1;
    public static final int CLOTHES = 2;
    public static final int FOOD = 3;
    public static final int GIFT = 4;
    public static final int HEALTH = 5;

    public static final int SALARY = 6;
    public static final int BONUS = 7;

    public static String getTypeString(int type){
        String typeString;
        switch (type){
            case SHOPPING :typeString = "Shopping";break;
            case CLOTHES:typeString = "Clothes";break;
            case FOOD :typeString = "Food";break;
            case GIFT:
                typeString = "Gift";
                break;
            case HEALTH:
                typeString = "Health";
                break;

            case SALARY:typeString = "Salary";break;
            case BONUS:typeString = "Bonus";break;

            default:typeString = "Other";
        }
        return typeString;
    }

}
