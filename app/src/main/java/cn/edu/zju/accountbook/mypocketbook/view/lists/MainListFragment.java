package cn.edu.zju.accountbook.mypocketbook.view.lists;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cn.edu.zju.accountbook.mypocketbook.R;
import cn.edu.zju.accountbook.mypocketbook.data.Record;
import cn.edu.zju.accountbook.mypocketbook.data.RecordLab;
import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;

import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.INCOME;
import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.getTypeString;

/**
 * Created by 张昊 on 2017/12/21.
 */
public class MainListFragment extends Fragment {

    private TextView mThisMonthTextView;
    private TextView mExpenditureAmountTextView;
    private TextView mIncomeAmountTextView;
    private TextView mSurplusAmountTextView;
    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> topics;
    private ArrayList<String> details;
    private MyAdapter adapter;
    private Bundle mBundle;

    public MainListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_one, container, false);
        mThisMonthTextView = view.findViewById(R.id.thisMonthTextView);
        mExpenditureAmountTextView = view.findViewById(R.id.outcomeAmountTextView);
        mIncomeAmountTextView = view.findViewById(R.id.incomeAmountTextView);
        mSurplusAmountTextView = view.findViewById(R.id.surplusAmountTextView);

        Date date = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        String thisMonth = monthFormat.format(date);
        mThisMonthTextView.setText(thisMonth);
        thisMonth = new SimpleDateFormat("MM").format(date);

        RecordLab recordLab = RecordLab.get(getActivity());
        mExpenditureAmountTextView.setText(String.valueOf(recordLab.getMonthExpenditure(thisMonth)));
        mIncomeAmountTextView.setText(String.valueOf(recordLab.getMonthIncome(thisMonth)));
        mSurplusAmountTextView.setText(String.valueOf(recordLab.getMonthSurplus(thisMonth)));

        listView = view.findViewById(R.id.main_list);

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
        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.refresh(); // 主动下拉刷新

        return view;
    }

    private void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    topics = new ArrayList<>();
                    details = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    listView.setRefreshSuccess("加载成功");
                    listView.startLoadMore();
                } catch (Exception e) {
                    listView.setRefreshFail("加载失败");
                }
            }
        }, 800);
    }

    private void loadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Date today = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(today);
                    RecordLab recordLab = RecordLab.get(getActivity());
                    List<Record> invertedRecords = recordLab.getInvertedRecords();
                    String detail = "";
                    float surplus = 0;
                    Iterator<Record> iterator = invertedRecords.iterator();
                    int num = 0;
                    HashSet<String> de = new HashSet<>();
                    while (iterator.hasNext()) {
                        Record r = iterator.next();
                        if (!r.getDateTime().substring(0, 10).equals(date)) {
                            String topic;
                            if (surplus > 0) {
                                topic = date + "   +" + String.valueOf(surplus);
                            } else {
                                topic = date + "   -" + String.valueOf(surplus);
                            }
                            topics.add(topic);
                            details.add(detail);
                            date = r.getDateTime().substring(0, 10);
                            detail = "";
                            surplus = 0;
                            num = 0;
                            de.clear();
                        }
                        if (r.getCategory() == INCOME) {
                            surplus += Float.valueOf(r.getAmount());
                        } else {
                            surplus -= Float.valueOf(r.getAmount());
                        }
                        if (de.size() < 3) {
                            if (!de.contains(getTypeString(r.getType()))) {
                                if (de.size() == 0) {
                                    detail = getTypeString(r.getType());
                                }
                                detail = detail + " / " + getTypeString(r.getType());
                                de.add(getTypeString(r.getType()));
                                num++;
                            }
                        } else if (de.size() == 3) {
                            detail = detail + " and so on";
                            de.add(" and so on");
                        }
                    }
                    String topic;
                    if (surplus > 0) {
                        topic = date + "    +" + String.valueOf(surplus);
                    } else {
                        topic = date + "    -" + String.valueOf(surplus);
                    }
                    topics.add(topic);
                    details.add(detail);
                } catch (Exception e) {
                    listView.setRefreshFail("加载失败");
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                listView.setLoadMoreSuccess();
                listView.stopLoadMore();
            }
        }, 500);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return topics == null ? 0 : topics.size();
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

            if (convertView == null) {
                textView = (TextView) getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_1, null);
                textView = (TextView) getLayoutInflater(mBundle).inflate(android.R.layout.simple_list_item_activated_1, null);
                relativeLayout = (RelativeLayout) getLayoutInflater(mBundle).inflate(R.layout.main_item_layout, null);
            } else {
                relativeLayout = (RelativeLayout) convertView;
            }
            itemTopic = relativeLayout.findViewById(R.id.main_item_topic);
            itemDetail = relativeLayout.findViewById(R.id.main_item_detail);
            itemTopic.setText(topics.get(position));
            itemDetail.setText(details.get(position));
            return relativeLayout;
        }
    }

}