package com.wangjessica.jwlab06b;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BottleFragment extends Fragment {
    ViewPager2 mViewPager2;
    int position;
    View view;
    String code;
    OnPuzzleSolved passer;
    // Puzzle 1 variables
    int filledLevel = 0;
    int targetLevel = 0;
    ImageView filled;
    ImageView target;
    Button fullSpoon;
    Button emptySpoon;
    ImageView fullSpoonImg;
    ImageView emptySpoonImg;
    TextView text;
    boolean solved = false;
    public static Fragment newInstance(ViewPager2 mViewPager2, int position, String code, boolean solved) {
        BottleFragment fragment = new BottleFragment();
        fragment.mViewPager2 = mViewPager2;
        fragment.position = position;
        fragment.code = code;
        fragment.solved = solved;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottle, container, false);
        // getResources().getIdentifier(view, "layout", "com.wangjessica.jwlab06b")
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Components
        filled = view.findViewById(R.id.filled_bottle);
        target = view.findViewById(R.id.target_bottle);
        fullSpoon = view.findViewById(R.id.spoon_over_target);
        fullSpoon.setOnClickListener(this::pourWater);
        emptySpoon = view.findViewById(R.id.spoon_over_filled);
        emptySpoon.setOnClickListener(this::transferWater);
        fullSpoonImg = view.findViewById(R.id.spoon_full);
        emptySpoonImg = view.findViewById(R.id.spoon_empty);
        text = view.findViewById(R.id.final_message);
        if(solved){
            fullSpoon.setClickable(false);
            emptySpoon.setClickable(false);
            text.setText(code);
            text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passer = (OnPuzzleSolved) context;
    }

    public void transferWater(View view){
        filledLevel++;
        filled.setBackgroundResource(getResources().getIdentifier("filled"+filledLevel, "drawable", "com.wangjessica.jwlab06b"));
        emptySpoon.setClickable(false);
        emptySpoonImg.setImageResource(R.drawable.spoonfull);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptySpoon.setVisibility(View.INVISIBLE);
                emptySpoonImg.setVisibility(View.INVISIBLE);
                emptySpoonImg.setImageResource(R.drawable.spoonempty);
                fullSpoon.setVisibility(View.VISIBLE);
                fullSpoonImg.setVisibility(View.VISIBLE);
                fullSpoon.setClickable(true);
            }
        }, 800);
    }
    public void pourWater(View view){
        targetLevel++;
        fullSpoon.setClickable(false);
        target.setBackgroundResource(getResources().getIdentifier("target"+targetLevel, "drawable", "com.wangjessica.jwlab06b"));
        fullSpoonImg.setBackgroundResource(R.drawable.spoonempty);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(targetLevel==3){
                    passSolved();
                    text.setText("You did it!\nCode: "+code);
                    text.setVisibility(View.VISIBLE);
                }
                else{
                    fullSpoon.setVisibility(View.INVISIBLE);
                    fullSpoonImg.setVisibility(View.INVISIBLE);
                    fullSpoonImg.setBackgroundResource(R.drawable.spoonfull);
                    emptySpoon.setVisibility(View.VISIBLE);
                    emptySpoonImg.setVisibility(View.VISIBLE);
                    emptySpoon.setClickable(true);
                }
            }
        }, 800);
    }
    public interface OnPuzzleSolved{
        void onPuzzleSolved(String code, int pos);
    }
    public void passSolved(){
        passer.onPuzzleSolved(code, position);
    }
}
