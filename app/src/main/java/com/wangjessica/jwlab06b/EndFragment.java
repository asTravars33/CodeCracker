package com.wangjessica.jwlab06b;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

import java.util.Locale;

public class EndFragment extends Fragment {
    ViewPager2 mViewPager2;
    int position;
    String[] codeStr;
    // Variables
    ImageView[] keys = new ImageView[4];
    TextView[] codes = new TextView[4];
    TextView error;
    TextView successMsg;
    ImageView trophy;
    public static Fragment newInstance(ViewPager2 mViewPager2, int position, String[] codes) {
        EndFragment fragment = new EndFragment();
        fragment.mViewPager2 = mViewPager2;
        fragment.position = position;
        fragment.codeStr = codes;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.end, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Variables
        System.out.println("CREATING VARIABLES");
        keys[0] = view.findViewById(R.id.key1);
        keys[1] = view.findViewById(R.id.key2);
        keys[2] = view.findViewById(R.id.key3);
        keys[3] = view.findViewById(R.id.key4);
        codes[0] = view.findViewById(R.id.code1);
        codes[1] = view.findViewById(R.id.code2);
        codes[2] = view.findViewById(R.id.code3);
        codes[3] = view.findViewById(R.id.code4);
        error = view.findViewById(R.id.incomplete);
        successMsg = view.findViewById(R.id.success_msg);
        trophy = view.findViewById(R.id.trophy);
        System.out.println("HERE I AM");
        showKeys();
    }
    public void showKeys(){
        System.out.println("SHOWING KEYS");
        for(int i=0; i<codeStr.length; i++){
            System.out.println(codeStr[i]);
            if(codeStr[i]==null){
                System.out.println("NULL STRING");
                error.setVisibility(View.VISIBLE);
                return;
            }
        }
        System.out.println("DONE HERE");
        error.setVisibility(View.GONE);
        for(int i=0; i<codeStr.length; i++){
            Handler handler = new Handler();
            final int j=i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    codes[j].setText(codeStr[j]);
                    keys[j].setVisibility(View.VISIBLE);
                    codes[j].setVisibility(View.VISIBLE);
                }
            }, 1000*i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                trophy.setVisibility(View.VISIBLE);
                successMsg.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }

    @Override
    public void onResume() {
        super.onResume();
        showKeys();
    }
}