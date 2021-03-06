package cn.edu.zju.accountbook.mypocketbook.view.lists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import cn.edu.zju.accountbook.mypocketbook.R;
import cn.edu.zju.accountbook.mypocketbook.data.Record;
import cn.edu.zju.accountbook.mypocketbook.data.RecordLab;
import cn.edu.zju.accountbook.mypocketbook.view.PhotoDialog;
import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;

import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.BONUS;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.CLOTHES;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.FOOD;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.GIFT;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.HEALTH;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.INCOME;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.OTHER;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.SALARY;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.SHOPPING;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.getTypeString;


public class ListFragment extends Fragment {
    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> topics;
    private ArrayList<String> details;
    private ArrayList<Integer> images;
    private int pageId = -1;
    private MyAdapter adapter;
    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.mBundle = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        listView = v.findViewById(R.id.fragment1_list_view);

        handler = new Handler();


        // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        /*
        float density = getResources().getDisplayMetrics().density;
        listView.setFirstTopOffset((int) (50 * density));
        */

        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getContext());
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getContext());
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);

        // 设置列表项出现动画（可选）
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.refresh(); // 主动下拉刷新


        /***
         * 点击显示明细
         */
        listView.setOnItemClickListener(new ZrcListView.OnItemClickListener() {
            @Override
            public void onItemClick(ZrcListView parent, View view, int position, long id) {
                PhotoDialog photoDialog = new PhotoDialog(getContext(),position);
                photoDialog.show();
            }
        });

        /***
         * 长按删除条目
         */
        listView.setOnItemLongClickListener(new ZrcListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(ZrcListView parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("删除");
                builder.setMessage("确定删除该条记录？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            RecordLab recordLab = RecordLab.get(getActivity());
                            UUID uuid = recordLab.getInvertedRecords().get(position).getId();
                            if(!recordLab.deleteRecord(uuid))
                                throw new Exception();
                        }catch (Exception e){
                            Toast.makeText(getContext(),"删除失败", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(),"删除成功", Toast.LENGTH_SHORT).show();
                        listView.refresh();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;//返回true代表LongClick事件发生时会忽略Click事件
            }
        });
        return v;
    }

    private void refresh(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    pageId = -1;
                    topics = new ArrayList<String>();
                    details = new ArrayList<>();
                    images = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    listView.setRefreshSuccess("加载成功"); // 通知加载成功
                    listView.startLoadMore(); // 开启LoadingMore功能
                }catch (Exception e) {listView.setRefreshFail("加载失败");}
            }
        }, 800);
    }

    private void loadMore(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId++;
                List<Record> invertedRecords = RecordLab.get(getActivity()).getInvertedRecords();
                Iterator<Record> iterator = invertedRecords.iterator();
                for(int i = 0 ;i<(pageId+1)*20&&iterator.hasNext();i++){
                    if(i<pageId*20){
                        iterator.next();
                        continue;
                    }
                    Record r = iterator.next();
                    String category = "-";
                    if(r.getCategory()==INCOME){
                        category = "+";
                    }
                    topics.add(getTypeString(r.getType())+"  "+ category+"  "+r.getAmount() + "元");
                    String location = r.getLocation();
                    String remark = r.getRemark();
                    String detail = r.getDetail();
                    if(location == null){
                        location = "";
                    }
                    if(remark == null || remark.equals("")){
                        remark = "";
                    }else {
                        remark = "\nRemark: "+remark;
                    }
                    if(detail == null){
                        detail = "";
                    }
                    details.add(detail + " "+ r.getDateTime() +" "+location + remark);
                    images.add(r.getType());
                }
                adapter.notifyDataSetChanged();
                listView.setLoadMoreSuccess();
                if(!iterator.hasNext()){
                    listView.stopLoadMore();
                }
            }
        }, 500);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return topics ==null ? 0 : topics.size();
        }
        @Override
        public Object getItem(int position) {
            return topics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView;
            TextView itemTopic;
            TextView itemDetail;
            ImageView imageView;
            RelativeLayout relativeLayout;

            if(convertView==null) {
                textView = (TextView) getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_1, null);
                textView = (TextView)getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_activated_1,null);
                relativeLayout = (RelativeLayout) getLayoutInflater(mBundle).inflate(R.layout.list_item_layout, null);
            }else{
                relativeLayout = (RelativeLayout)convertView;
            }
            itemTopic = relativeLayout.findViewById(R.id.list_item_topic);
            itemDetail = relativeLayout.findViewById(R.id.list_item_detail);
            imageView = relativeLayout.findViewById(R.id.list_image);
            itemTopic.setText(topics.get(position));
            itemDetail.setText(details.get(position));
            switch (images.get(position)) {
                case OTHER:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.add));
                    break;
                case SHOPPING:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.shopping));
                    break;
                case CLOTHES:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.clothing));
                    break;
                case FOOD:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.eating));
                    break;
                case GIFT:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.gift));
                    break;
                case HEALTH:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.health));
                    break;
                case SALARY:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.rmb));
                    break;
                case BONUS:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.profit));
                    break;
            }
            return relativeLayout;
        }
    }

}
