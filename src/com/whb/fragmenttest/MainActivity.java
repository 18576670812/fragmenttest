package com.whb.fragmenttest;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    public static String[] array = { "text1,", "text2", "text3", "text4",
            "text5,", "text6", "text7", "text8" };
    static Context mContext = null;
    static String TAG = "FragmentTest";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContext = (Context)this;
        /*
        Object service = getSystemService ("statusbar");
        try { 
			Class <?> statusBarManager = Class.forName
					("android.app.StatusBarManager"); 
			Method expand = statusBarManager.getMethod ("disable",int.class); 
			expand.invoke (service,0x00000001); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
        Log.d(TAG, "MainActivity onCreate, disable statusbar");
        */
        Pattern p = Pattern.compile("((.)\\2*)");
		String s="122aa,,,,    s09";
		Matcher m=p.matcher(s);
		while(m.find()){
			Log.d(TAG, "{"+m.group()+"}");
		}
		
		Device.initialize(getApplicationContext());
		Device device = Device.getInstance();
		device.showSystembar(false);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.options, menu); 
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
    public static class TitlesFragment extends ListFragment {
        boolean mDualPane;
        int mCurCheckPosition = 0;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            System.out.println("Fragment-->onCreate");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // TODO Auto-generated method stub
        	System.out.println("Fragment-->onCreateView");
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onPause() {
            // TODO Auto-generated method stub
            super.onPause();
            System.out.println("Fragment-->onPause");
        }

        @Override
        public void onStop() {
            // TODO Auto-generated method stub
            super.onStop();

            System.out.println("Fragment-->onStop");
            
            Intent intent = new Intent("com.whb.fragmenttest.START_RECORD");
            System.out.println("send broadcast: " + intent);
            mContext.sendBroadcast(intent);
        }

        @Override
        public void onAttach(Activity activity) {
            // TODO Auto-generated method stub
            super.onAttach(activity);

            System.out.println("Fragment-->onAttach");
        }

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            super.onStart();

            System.out.println("Fragment-->onStart");
        }

        @Override
        public void onResume() {
            // TODO Auto-generated method stub
            super.onResume();

            System.out.println("Fragment-->onResume");
        }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();

            System.out.println("Fragment-->onDestroy");
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onActivityCreated(savedInstanceState);

            System.out.println("Fragment-->onActivityCreted");
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, array));

            View detailsFrame = getActivity().findViewById(R.id.details);

            mDualPane = detailsFrame != null
                    && detailsFrame.getVisibility() == View.VISIBLE;

            if (savedInstanceState != null) {
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0); 
                //从保存的状态中取出数据
            }

            if (mDualPane) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                showDetails(mCurCheckPosition);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            // TODO Auto-generated method stub
            super.onSaveInstanceState(outState);

            outState.putInt("curChoice", mCurCheckPosition);//保存当前的下标
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // TODO Auto-generated method stub
            super.onListItemClick(l, v, position, id);

            showDetails(position);
        }

        void showDetails(int index) {
            mCurCheckPosition = index;
            if (mDualPane) {
                getListView().setItemChecked(index, true);
                DetailsFragment details = (DetailsFragment) getFragmentManager()
                        .findFragmentById(R.id.details);

                if (details == null || details.getShownIndex() != index) {
                    details = DetailsFragment.newInstance(mCurCheckPosition);

                    //得到一个fragment 事务（类似sqlite的操作）
                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.details, details);
                    //将得到的fragment 替换当前的viewGroup内容，add则不替换会依次累加
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    //设置动画效果
                    ft.commit();//提交
                }
            } else {
                new AlertDialog.Builder(getActivity()).setTitle(
                        android.R.string.dialog_alert_title).setMessage(
                        array[index]).setPositiveButton(android.R.string.ok,
                        null).show();
            }
        }
    }
 
    /**
     * 作为界面的一部分，为fragment 提供一个layout
     * @author terry
     *
     */

    @SuppressLint("NewApi") 
    public static class DetailsFragment extends Fragment {
        @SuppressLint("NewApi") 
        public static DetailsFragment newInstance(int index) {
            DetailsFragment details = new DetailsFragment();
            Bundle args = new Bundle();
            args.putInt("index", index);
            details.setArguments(args);
            return details;
        }

        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            if (container == null)
                return null;

            ScrollView scroller = new ScrollView(getActivity());
            TextView text = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getActivity()
                            .getResources().getDisplayMetrics());
            text.setPadding(padding, padding, padding, padding);
            scroller.addView(text);
            text.setText(array[getShownIndex()]);

            return scroller;
        }
    }
}
