package com.wangjessica.jwlab06b;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

import java.util.Locale;

public class HolesFragment extends Fragment {
    ViewPager2 mViewPager2;
    int position;
    String code;
    OnPuzzleSolved passer;
    // Layout components
    int holeIdx;
    ImageView msg;
    ImageView holes;
    ImageView msgAns;
    ImageView chest;
    TextView message;
    EditText input;
    TextView note;
    Button submitPass;
    String[] passwords = {"", "greeting", "ready", "open", "hello"};
    boolean dropped;
    public static Fragment newInstance(ViewPager2 mViewPager2, int position, String code) {
        HolesFragment fragment = new HolesFragment();
        fragment.mViewPager2 = mViewPager2;
        fragment.position = position;
        fragment.code = code;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.holes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Variables
        msg = view.findViewById(R.id.msg);
        msgAns = view.findViewById(R.id.msg_reveal);
        holes = view.findViewById(R.id.holes);
        submitPass = view.findViewById(R.id.submit_pass);
        message = view.findViewById(R.id.final_message);
        note = view.findViewById(R.id.top_text);
        input = view.findViewById(R.id.password);
        chest = view.findViewById(R.id.chest);
        holeIdx = (int)(Math.random()*4)+1;
        System.out.println("holes"+holeIdx);
        holes.setBackgroundResource(getResources().getIdentifier("holes"+holeIdx, "drawable", "com.wangjessica.jwlab06b"));
        // Event listeners
        submitPass.setOnClickListener(this::validate);
        holes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData.Item item = new ClipData.Item("holes");
                ClipData dragData = new ClipData("holes", new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
                View.DragShadowBuilder shadow = new DragShadow(view);
                view.startDragAndDrop(dragData, shadow, null, 0);
                return true;
            }
        });
        msg.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch(dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        holes.setVisibility(View.INVISIBLE);
                        System.out.println("Drag started");
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        System.out.println("Entered");
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        System.out.println("Exited");
                        return true;
                    case DragEvent.ACTION_DROP:
                        msgAns.setImageResource(getResources().getIdentifier("holes"+holeIdx+"ans", "drawable", "com.wangjessica.jwlab06b"));
                        msgAns.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.GONE);
                        note.setVisibility(View.INVISIBLE);
                        dropped = true;
                        System.out.println("Dropped");
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(!dropped)
                            holes.setVisibility(View.VISIBLE);
                        System.out.println("Ended");
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passer = (OnPuzzleSolved) context;
    }

    private class DragShadow extends View.DragShadowBuilder{
        Drawable shadow = ResourcesCompat.getDrawable(getContext().getResources(), getResources().getIdentifier("holes"+holeIdx, "drawable", "com.wangjessica.jwlab06b"), null);
        public DragShadow(View view){
            super(view);
        }
        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);
            int width, height;
            width = getView().getWidth()/2;
            height = getView().getHeight()/2;
            shadow.setBounds(0, 0, width, height);
            outShadowSize.set(width, height);
            outShadowTouchPoint.set(width/2, height/2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }
    public void validate(View view){
        String entered = input.getText().toString();
        if(entered.toLowerCase(Locale.ROOT).equals(passwords[holeIdx])){
            chest.setBackgroundResource(R.drawable.chestopened);
            message.setText("Correct! Code: "+code);
            message.setVisibility(View.VISIBLE);
            passSolved();
        }
        else{
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

    public interface OnPuzzleSolved{
        void onPuzzleSolved(String code, int pos);
    }
    public void passSolved(){
        passer.onPuzzleSolved(code, position);
    }
}