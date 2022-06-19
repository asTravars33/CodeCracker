package com.wangjessica.jwlab06b;

import android.content.Context;
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

public class CipherFragment extends Fragment {
    ViewPager2 mViewPager2;
    int position;
    View view;
    String code;
    OnPuzzleSolved passer;
    // Layout components
    ImageView[] clues = new ImageView[4];
    EditText[] edits = new EditText[4];
    TextView message;
    public static Fragment newInstance(ViewPager2 mViewPager2, int position, String code) {
        CipherFragment fragment = new CipherFragment();
        fragment.mViewPager2 = mViewPager2;
        fragment.position = position;
        fragment.code = code;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cipher, container, false);
        clues[0] = view.findViewById(R.id.first);
        clues[1]= view.findViewById(R.id.second);
        clues[2] = view.findViewById(R.id.third);
        clues[3] = view.findViewById(R.id.fourth);
        for(int i=1; i<=code.length(); i++){
            String pic = "clue"+code.substring(i-1, i).toLowerCase(Locale.ROOT);
            System.out.println(pic);
            clues[i-1].setImageResource(getResources().getIdentifier(pic, "drawable", "com.wangjessica.jwlab06b"));
        }
        edits[0] = view.findViewById(R.id.edit1);
        edits[1] = view.findViewById(R.id.edit2);
        edits[2]= view.findViewById(R.id.edit3);
        edits[3] = view.findViewById(R.id.edit4);
        message = view.findViewById(R.id.final_message);
        Button click = view.findViewById(R.id.check);
        click.setOnClickListener(this::validate);
        return view;
        // getResources().getIdentifier(view, "layout", "com.wangjessica.jwlab06b")
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passer = (OnPuzzleSolved) context;
    }

    public void validate(View view){
        boolean done = false;
        for(int i=0; i<edits.length&&!done; i++){
            if(!edits[i].getText().toString().equals(code.substring(i, i+1))){
                // It's wrong
                done = true;
                message.setText("Incorrect");
                message.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        message.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        }
        if(!done){
            passSolved();
            message.setText("Correct! Code: "+code);
            message.setVisibility(View.VISIBLE);
        }
    }

    public interface OnPuzzleSolved{
        void onPuzzleSolved(String code, int pos);
    }
    public void passSolved(){
        passer.onPuzzleSolved(code, position);
    }
}
