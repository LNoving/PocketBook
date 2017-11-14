package cn.edu.zju.accountbook.accountbookdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.TextView;


import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;



public class Fragment1 extends Fragment {
    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> msgs;
    private int pageId = -1;
    private MyAdapter adapter;
    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.mBundle = savedInstanceState;                   //乱加的
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

        return v;

    //    return inflater.inflate(R.layout.fragment_1, container, false);
    }

    private void refresh(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{

                    pageId = 0;
                    msgs = new ArrayList<String>();
                    List<Record> invertedRecords = RecordLab.get(getActivity()).getInvertedRecords();
                    Iterator<Record> iterator = invertedRecords.iterator();
                    for(int i = 0;i<20&&iterator.hasNext();i++){
                        Record r = iterator.next();
                        msgs.add(r.getId()+r.getType()+r.getPurpose()+r.getAmount()+
                                r.getDateTime()+r.getAddress()+r.getLocation()+r.getPhoto());
                    }
                    adapter.notifyDataSetChanged();
                    listView.setRefreshSuccess("加载成功"); // 通知加载成功
                    listView.startLoadMore(); // 开启LoadingMore功能
                }catch (Exception e) {listView.setRefreshFail("加载失败");}
            }
        }, 2 * 1000);
    }

    private void loadMore(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId++;

                List<Record> invertedRecords = RecordLab.get(getActivity()).getInvertedRecords();
                Iterator<Record> iterator = invertedRecords.iterator();
                for(int i = 0 ;i<(pageId+1)*20&&iterator.hasNext();i++){
                    if(i<pageId*20)
                        continue;
                    Record r = iterator.next();
                    msgs.add(r.getId()+r.getType()+r.getPurpose()+r.getAmount()+
                            r.getDateTime()+r.getAddress()+r.getLocation()+r.getPhoto());
                }
                if(iterator.hasNext()){
                    adapter.notifyDataSetChanged();
                    listView.setLoadMoreSuccess();
                }
                else {
                    listView.stopLoadMore();
                }

            }
        }, 2 * 1000);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs==null ? 0 : msgs.size();
        }
        @Override
        public Object getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if(convertView==null) {
                textView = (TextView) getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_1, null);
            }else{
                textView = (TextView) convertView;
            }
            textView.setText(msgs.get(position));
            return textView;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
