package com.way.pinnedheaderlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.way.view.BladeView;
import com.way.view.BladeView.OnItemClickListener;
import com.way.view.PinnedHeaderListView;

public class MainActivity extends Activity {
	private static final String FORMAT = "^[a-z,A-Z].*$";
	private PinnedHeaderListView mListView;
	private BladeView mLetter;
	private FriendsAdapter mAdapter;
	private String[] datas;
	// ����ĸ��
	private List<String> mSections;
	// ��������ĸ�������
	private Map<String, List<String>> mMap;
	// ����ĸλ�ü�
	private List<Integer> mPositions;
	// ����ĸ��Ӧ��λ��
	private Map<String, Integer> mIndexer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initView();
	}

	private void initData() {
		datas = getResources().getStringArray(R.array.countries);
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<String>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();

		for (int i = 0; i < datas.length; i++) {
			String firstName = datas[i].substring(0, 1);
			if (firstName.matches(FORMAT)) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(datas[i]);
				} else {
					mSections.add(firstName);
					List<String> list = new ArrayList<String>();
					list.add(datas[i]);
					mMap.put(firstName, list);
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(datas[i]);
				} else {
					mSections.add("#");
					List<String> list = new ArrayList<String>();
					list.add(datas[i]);
					mMap.put("#", list);
				}
			}
		}

		Collections.sort(mSections);
		int position = 0;
		for (int i = 0; i < mSections.size(); i++) {
			mIndexer.put(mSections.get(i), position);// ����map�У�keyΪ����ĸ�ַ�����valueΪ����ĸ��listview��λ��
			mPositions.add(position);//// ����ĸ��listview��λ�ã�����list��
			position += mMap.get(mSections.get(i)).size();// ������һ������ĸ��listview��λ��
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView = (PinnedHeaderListView) findViewById(R.id.friends_display);
		mLetter = (BladeView) findViewById(R.id.friends_myletterlistview);
		mLetter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mListView.setSelection(mIndexer.get(s));
				}
			}
		});
		mAdapter = new FriendsAdapter(this, datas, mSections, mPositions);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_head, mListView, false));
	}

}
