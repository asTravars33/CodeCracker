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

import java.util.HashSet;
import java.util.Locale;

public class PoemFragment extends Fragment {
    ViewPager2 mViewPager2;
    int position;
    String code;
    OnPuzzleSolved passer;
    // Layout details
    TextView message;
    TextView poem;
    String[] poems = {"when the clouds darken", "we'll walk into the rain with", "the world in our palms", "birds awakening,", "flowers drifting from the sky-", "brilliant sunrise"};
    String[] poemPics;
    ImageView[] images;
    String[] imagePics;
    String[] oriPics;
    Button[] forwardButtons;
    Button[] backButtons;
    public static Fragment newInstance(ViewPager2 mViewPager2, int position, String code) {
        PoemFragment fragment = new PoemFragment();
        fragment.mViewPager2 = mViewPager2;
        fragment.position = position;
        fragment.code = code;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.poem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Variables
        message = view.findViewById(R.id.final_message);
        poem = view.findViewById(R.id.poem);
        poemPics = getResources().getStringArray(R.array.poem_pics);
        images = new ImageView[3];
        images[0] = view.findViewById(R.id.img1);
        images[1] = view.findViewById(R.id.img2);
        images[2] = view.findViewById(R.id.img3);
        forwardButtons = new Button[3];
        forwardButtons[0] = view.findViewById(R.id.forward1);
        forwardButtons[1] = view.findViewById(R.id.forward2);
        forwardButtons[2] = view.findViewById(R.id.forward3);
        backButtons = new Button[3];
        backButtons[0] = view.findViewById(R.id.back1);
        backButtons[1] = view.findViewById(R.id.back2);
        backButtons[2] = view.findViewById(R.id.back3);
        // Set a random poem and sequence of images
        int idx = (int)(Math.random()*(poems.length/3));
        int ind = 0;
        String msg = "";
        oriPics = new String[3];
        for(int i=3*idx; i<3*idx+3; i++){
            msg = msg+poems[i]+"\n";
            oriPics[ind++] = poemPics[i];
        }
        poem.setText(msg);
        ind = 0;
        HashSet<Integer> done = new HashSet<Integer>();
        imagePics = new String[oriPics.length];
        while(done.size()<oriPics.length){
            int id = (int)(Math.random()*oriPics.length);
            while(done.contains(id)||id==ind){
                id = (int)(Math.random()*oriPics.length);
            }
            done.add(id);
            imagePics[ind] = oriPics[id];
            images[ind].setImageResource(getResources().getIdentifier(oriPics[id], "drawable", "com.wangjessica.jwlab06b"));
            ind++;
        }
        // On Click Listeners
        for(Button b: forwardButtons){
            b.setOnClickListener(this::moveImageRight);
           b.setText(">");
        }
        for(Button b: backButtons){
            b.setOnClickListener(this::moveImageLeft);
            b.setText("<");
        }
        Button check = view.findViewById(R.id.check);
        check.setOnClickListener(this::validate);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passer = (OnPuzzleSolved) context;
    }

    public void moveImageLeft(View view){
        Button pressed = (Button) view;
        int idx = Integer.parseInt(pressed.getTag().toString())-1;
        if(idx!=0){
            String temp = imagePics[idx-1];
            imagePics[idx-1] = imagePics[idx];
            imagePics[idx] = temp;
        }
        redisplay();
    }
    public void moveImageRight(View view){
        Button pressed = (Button) view;
        int idx = Integer.parseInt(pressed.getTag().toString())-1;
        if(idx!=imagePics.length-1){
            String temp = imagePics[idx+1];
            imagePics[idx+1] = imagePics[idx];
            imagePics[idx] = temp;
        }
        redisplay();
    }
    public void redisplay(){
        for(int i=0; i<imagePics.length; i++){
            images[i].setImageResource(getResources().getIdentifier(imagePics[i], "drawable", "com.wangjessica.jwlab06b"));
        }
    }
    public void validate(View view){
        boolean done = false;
        for(int i=0; i<imagePics.length; i++){
            if(!imagePics[i].equals(oriPics[i])){
                // It's wrong
                done = true;
                message.setText("Incorrect!");
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
