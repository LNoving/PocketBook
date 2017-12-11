package cn.edu.zju.accountbook.accountbookdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;



public class Fragment1 extends Fragment {
    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> topics;
    private ArrayList<String> details;
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
        View v = inflater.inflate(R.layout.fragment_1, container, false);

        listView = (ZrcListView) v.findViewById(R.id.fragment1_list_view);

        handler = new Handler();


        // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        /***
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
                Toast.makeText(getContext(),String.valueOf(position), Toast.LENGTH_SHORT).show();
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
                    topics.add(r.getTypeString()+"   "+ r.getAmount() + "元");
                    details.add(r.getPurpose() + " " + r.getDateTime() + r.getLocation());
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
            RelativeLayout relativeLayout;

            if(convertView==null) {
                textView = (TextView) getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_1, null);
                textView = (TextView)getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_activated_1,null);
                relativeLayout = (RelativeLayout)getLayoutInflater(mBundle).inflate(R.layout.item_layout,null);

                Log.v("显示","convertView == null");
            }else{
                //textView = (TextView) convertView;
                relativeLayout = (RelativeLayout)convertView;
                Log.v("显示","convertView == null");
            }
            itemTopic = relativeLayout.findViewById(R.id.item_topic);
            itemDetail = relativeLayout.findViewById(R.id.item_detail);
            itemTopic.setText(topics.get(position));
            itemDetail.setText(details.get(position));
            return relativeLayout;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
