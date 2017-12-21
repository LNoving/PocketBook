package cn.edu.zju.accountbook.accountbookdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

import cn.edu.zju.accountbook.accountbookdemo.R;
import cn.edu.zju.accountbook.accountbookdemo.data.Record;
import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;
import cn.edu.zju.accountbook.accountbookdemo.exception.NullException;

/**
 * Created by 张昊 on 2017/12/9.
 */

public class PhotoDialog extends Dialog {
    private Context mContext;
    private ImageView photo;
    private TextView nophotos;
    private int position;
    public PhotoDialog (Context context){
        super(context);
        mContext = context;
    }
    public PhotoDialog(Context context, int theme,int position) {
        super(context, theme);
        mContext = context;
        this.position = position;

    }
    public PhotoDialog(Context context,int position) {
        super(context);
        mContext = context;
        this.position = position;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.photo_dialog, null);
        photo = layout.findViewById(R.id.photo_);
        try{
            List<Record> records = RecordLab.get(getOwnerActivity()).getInvertedRecords();
            Record r = records.get(position);
            if(r.getPhoto()==null)
                throw new NullException();
            else {
                Uri uri = Uri.parse(r.getPhoto());
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getContext().getContentResolver().openInputStream(uri));
                photo.setImageBitmap(bitmap);
            }
        }catch (NullException e){
            e.printStackTrace();
            layout = inflater.inflate(R.layout.photo_not_found, null);
            nophotos = layout.findViewById(R.id.no_photo);
            nophotos.setText("当前条目没有记录图片");
        }catch (FileNotFoundException e){
            e.printStackTrace();
            layout = inflater.inflate(R.layout.photo_not_found, null);
            nophotos = layout.findViewById(R.id.no_photo);
            nophotos.setText("查找图片文件出错");
        }
        this.setContentView(layout);
    }
}
