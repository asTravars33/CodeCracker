package com.wangjessica.jwlab06b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements BottleFragment.OnPuzzleSolved, CipherFragment.OnPuzzleSolved, PoemFragment.OnPuzzleSolved, HolesFragment.OnPuzzleSolved{
    ViewPager2 mViewPager2;
    MyFragmentStateAdapter mMyFragmentStateAdapter;
    int NUM_ITEMS = 5;
    String[] lets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    String[] lets_selective = {"E", "Y", "K", "S"};
    String[] digs_selective = {"2", "5", "7", "9"};
    String[] clues;
    String[] codes = new String[4];
    Button toggleHints;
    TextView hints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager2
        mViewPager2 = findViewById(R.id.container);
        mMyFragmentStateAdapter = new MyFragmentStateAdapter(this);
        mViewPager2.setAdapter(mMyFragmentStateAdapter);
        // Generate random clues
        clues = new String[NUM_ITEMS];
        for(int i=0; i<clues.length; i++){
            clues[i] = lets[(int)(Math.random()*26)] + (int)(Math.random()*10) + lets[(int)(Math.random()*26)] + (int)(Math.random()*10);
        }
        String clue = lets_selective[(int)(Math.random()*4)] + digs_selective[(int)(Math.random()*4)] + lets_selective[(int)(Math.random()*4)] + digs_selective[(int)(Math.random()*4)];
        clues[1] = clue;
        // Tab Layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, mViewPager2,
                (tab, position) -> tab.setText("Code "+(position+1))
        ).attach();
        tabLayout.getTabAt(4).setText("Final");
        // Other components
        toggleHints = findViewById(R.id.hints_button);
        hints = findViewById(R.id.hints);
    }

    @Override
    public void onPuzzleSolved(String code, int pos) {
        codes[pos] = code;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.setText(code);
        for(String s: codes) System.out.println(s);
        if(mMyFragmentStateAdapter.fragments.length>4&&mMyFragmentStateAdapter.fragments[4]!=null){
            System.out.println("Updating here");
            EndFragment cur = (EndFragment) mMyFragmentStateAdapter.fragments[4];
            cur.codeStr = codes;
            for(String s: cur.codeStr) System.out.println("Step "+s);
        }
    }

    public void toggleHints(View view) {
        String state = toggleHints.getText().toString();
        if(state.equals("Show Hints")){
            toggleHints.setText("Hide Hints");
            hints.setVisibility(View.VISIBLE);
        }
        else{
            toggleHints.setText("Show Hints");
            hints.setVisibility(View.GONE);
        }
    }

    private class MyFragmentStateAdapter extends FragmentStateAdapter {
        Fragment[] fragments = new Fragment[5];
        public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            System.out.println("Going to: "+position);
            switch(position){
                case 0:
                    fragments[position] = BottleFragment.newInstance(mViewPager2, position, clues[position], codes[0]!=null?true:false);
                    return fragments[position];
                case 1:
                    fragments[position] = CipherFragment.newInstance(mViewPager2, position, clues[position]);
                    return fragments[position];
                case 2:
                    fragments[position] = PoemFragment.newInstance(mViewPager2, position, clues[position]);
                    return fragments[position];
                case 3:
                    fragments[position] = HolesFragment.newInstance(mViewPager2, position, clues[position]);
                    return fragments[position];
                case 4:
                    fragments[position] = EndFragment.newInstance(mViewPager2, position, codes);
                    return fragments[position];
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            // Number of times you can swipe
            return NUM_ITEMS;
        }
    }
}