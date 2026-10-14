package com.example.haydayassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView status;
    CountDownTimer timer;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUI();
    }

    int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density); }

    TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    void buildUI() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(22), dp(18), dp(28));
        root.setBackgroundColor(Color.rgb(246, 250, 241));
        scroll.addView(root);
        setContentView(scroll);

        TextView title = text("🌾 Hay Day Farm Assistant", 24, Color.rgb(39, 92, 48), true);
        root.addView(title);
        TextView subtitle = text("ផែនការកសិដ្ឋាន • Android", 14, Color.DKGRAY, false);
        subtitle.setPadding(0, dp(5), 0, dp(16)); root.addView(subtitle);

        status = text("ស្ថានភាព៖ រួចរាល់សម្រាប់រៀបចំផែនការ", 15, Color.rgb(45, 90, 50), true);
        card(status);

        addSection("🌱 ដំណាំ និងពេលប្រមូលផល");
        addAction("ស្រូវសាលី (Wheat) — រំលឹកក្នុង 2 នាទី", 2);
        addAction("ពោត (Corn) — រំលឹកក្នុង 5 នាទី", 5);
        addAction("សណ្ដែកសៀង (Soybeans) — រំលឹកក្នុង 10 នាទី", 10);

        addSection("🐔 ការងារកសិដ្ឋាន");
        addNote("• ពិនិត្យចំណីមាន់ និងប្រមូលពង");
        addNote("• ពិនិត្យទឹកដោះគោ និងចំណីគោ");
        addNote("• ពិនិត្យ Feed Mill និងរោងចក្រផលិត");
        addNote("• ពិនិត្យកប៉ាល់ និងការបញ្ជាទិញឡាន");

        addSection("⏱️ កម្មវិធីរំលឹកផ្ទាល់ខ្លួន");
        EditText minutes = new EditText(this);
        minutes.setSingleLine(true);
        minutes.setInputType(2);
        minutes.setHint("បញ្ចូលចំនួននាទី");
        root.addView(minutes, new LinearLayout.LayoutParams(-1, dp(52)));
        Button set = button("ចាប់ផ្ដើមរំលឹក");
        root.addView(set);
        set.setOnClickListener(v -> {
            String value = minutes.getText().toString().trim();
            if (value.isEmpty()) { minutes.setError("សូមបញ្ចូលចំនួននាទី"); return; }
            try {
                int m = Integer.parseInt(value);
                if (m < 1 || m > 1440) { minutes.setError("បញ្ចូល 1 ដល់ 1440 នាទី"); return; }
                startTimer(m);
            } catch (Exception e) { minutes.setError("លេខមិនត្រឹមត្រូវ"); }
        });

        TextView disclaimer = text("កម្មវិធីនេះជាជំនួយរៀបចំផែនការ និងរំលឹកប៉ុណ្ណោះ។ វាមិនចុច ឬប្រមូលផលក្នុង Hay Day ដោយស្វ័យប្រវត្តិទេ។", 12, Color.GRAY, false);
        disclaimer.setPadding(0, dp(18), 0, 0);
        root.addView(disclaimer);
    }

    void card(View v) {
        v.setPadding(dp(12), dp(12), dp(12), dp(12));
        v.setBackgroundColor(Color.rgb(226, 240, 218));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(12); root.addView(v, p);
    }

    void addSection(String s) {
        TextView t = text(s, 18, Color.rgb(39, 92, 48), true);
        t.setPadding(0, dp(12), 0, dp(8)); root.addView(t);
    }
    void addNote(String s) {
        TextView t = text(s, 15, Color.DKGRAY, false);
        t.setPadding(dp(4), dp(6), dp(4), dp(6)); root.addView(t);
    }
    Button button(String s) {
        Button b = new Button(this); b.setText(s); b.setAllCaps(false);
        b.setTextColor(Color.WHITE); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(52, 120, 61)));
        return b;
    }
    void addAction(String label, int mins) {
        Button b = button(label);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(7); root.addView(b, p);
        b.setOnClickListener(v -> startTimer(mins));
    }
    void startTimer(int mins) {
        if (timer != null) timer.cancel();
        long duration = mins * 60_000L;
        status.setText("⏳ រំលឹកក្នុង " + mins + " នាទី");
        timer = new CountDownTimer(duration, 1000) {
            public void onTick(long ms) {
                long sec = ms / 1000;
                status.setText(String.format(Locale.getDefault(), "⏳ នៅសល់ %02d:%02d", sec/60, sec%60));
            }
            public void onFinish() {
                status.setText("🔔 ដល់ពេលពិនិត្យកសិដ្ឋានហើយ!");
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("ដល់ពេលហើយ!")
                    .setMessage("សូមបើក Hay Day ដើម្បីពិនិត្យដំណាំ ឬការងាររបស់អ្នក។")
                    .setPositiveButton("យល់ព្រម", (d,w) -> d.dismiss())
                    .show();
            }
        }.start();
    }
}
