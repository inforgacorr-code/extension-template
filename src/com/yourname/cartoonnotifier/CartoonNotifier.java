package com.yourname.cartoonnotifier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;

@DesignerComponent(
    version = 1,
    description = "Cartoon styled dialog for Sound Quiz — blue background, rounded, bouncy!",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
public class CartoonNotifier extends AndroidNonvisibleComponent {

    private final Context context;
    private final Form form;

    public CartoonNotifier(ComponentContainer container) {
        super(container.$form());
        this.form  = container.$form();
        this.context = container.$context();
    }

    // ─────────────────────────────────────────────────────────────
    //  EVENT — sama persis dengan Notifier1.AfterChoosing
    // ─────────────────────────────────────────────────────────────
    @SimpleEvent(description = "Dipanggil setelah user memilih tombol. choice = teks tombol yang ditekan.")
    public void AfterChoosing(String choice) {
        EventDispatcher.dispatchEvent(this, "AfterChoosing", choice);
    }

    // ─────────────────────────────────────────────────────────────
    //  FUNCTION — ShowCartoonDialog
    // ─────────────────────────────────────────────────────────────
    @SimpleFunction(description = "Tampilkan dialog kartun dengan 2 tombol. "
        + "button1 = tombol hijau (aksi utama), button2 = tombol merah (batal/keluar).")
    public void ShowCartoonDialog(
        final String title,
        final String message,
        final String button1Text,
        final String button2Text
    ) {
        form.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildAndShow(title, message, button1Text, button2Text);
            }
        });
    }

    // ─────────────────────────────────────────────────────────────
    //  INTERNAL — build dialog
    // ─────────────────────────────────────────────────────────────
    private void buildAndShow(
        final String title,
        final String message,
        final String btn1Label,
        final String btn2Label
    ) {
        // ── Root card ───────────────────────────────────────────
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(28), dp(28), dp(28), dp(24));

        GradientDrawable cardBg = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{Color.parseColor("#1976D2"), Color.parseColor("#0D47A1")}
        );
        cardBg.setCornerRadius(dp(28));
        cardBg.setStroke(dp(4), Color.parseColor("#FFD700")); // gold border
        card.setBackground(cardBg);

        // ── Judul ───────────────────────────────────────────────
        TextView tvTitle = new TextView(context);
        tvTitle.setText("⭐  " + title + "  ⭐");
        tvTitle.setTextColor(Color.parseColor("#FFD700"));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setPadding(0, 0, 0, dp(12));
        card.addView(tvTitle);

        // ── Garis pembatas ──────────────────────────────────────
        View divider = new View(context);
        LinearLayout.LayoutParams divParams =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(2));
        divParams.bottomMargin = dp(14);
        divider.setBackgroundColor(Color.parseColor("#FFD70055"));
        card.addView(divider, divParams);

        // ── Pesan ───────────────────────────────────────────────
        TextView tvMsg = new TextView(context);
        tvMsg.setText(message);
        tvMsg.setTextColor(Color.WHITE);
        tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvMsg.setGravity(Gravity.CENTER);
        tvMsg.setPadding(0, 0, 0, dp(24));
        card.addView(tvMsg);

        // ── Tombol baris ────────────────────────────────────────
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams btnP =
            new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        btnP.setMargins(dp(6), 0, dp(6), 0);

        // Tombol 1 — HIJAU (aksi utama: Main Lagi / Coba Lagi)
        final Button btn1 = makeButton(btn1Label,
            Color.parseColor("#00C853"), Color.parseColor("#B9F6CA"));

        // Tombol 2 — MERAH (batal: Keluar)
        final Button btn2 = makeButton(btn2Label,
            Color.parseColor("#D32F2F"), Color.parseColor("#FFCDD2"));

        row.addView(btn1, btnP);
        row.addView(btn2, btnP);
        card.addView(row);

        // ── Dialog ──────────────────────────────────────────────
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(card);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // ── Listener tombol ─────────────────────────────────────
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                AfterChoosing(btn1Label);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                AfterChoosing(btn2Label);
            }
        });

        dialog.show();

        // ── Animasi bounce saat muncul ──────────────────────────
        card.setScaleX(0.3f);
        card.setScaleY(0.3f);
        card.setAlpha(0f);

        AnimatorSet anim = new AnimatorSet();
        anim.playTogether(
            ObjectAnimator.ofFloat(card, "scaleX", 0.3f, 1f),
            ObjectAnimator.ofFloat(card, "scaleY", 0.3f, 1f),
            ObjectAnimator.ofFloat(card, "alpha",  0f,   1f)
        );
        anim.setDuration(420);
        anim.setInterpolator(new OvershootInterpolator(1.5f));
        anim.start();
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER — bikin tombol bergaya kartun
    // ─────────────────────────────────────────────────────────────
    private Button makeButton(String label, int bgColor, int shadowColor) {
        Button btn = new Button(context);
        btn.setText(label);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setPadding(dp(12), dp(14), dp(12), dp(14));
        btn.setAllCaps(false);

        // Tombol dengan shadow / inset bawah kartun-style
        GradientDrawable bg = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{lighten(bgColor, 0.15f), bgColor}
        );
        bg.setCornerRadius(dp(24));
        bg.setStroke(dp(3), Color.WHITE);
        btn.setBackground(bg);

        return btn;
    }

    // ─────────────────────────────────────────────────────────────
    //  UTIL
    // ─────────────────────────────────────────────────────────────
    private int dp(int val) {
        return Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, val,
            context.getResources().getDisplayMetrics()));
    }

    /** Perjelas warna untuk gradient tombol */
    private int lighten(int color, float factor) {
        float r = Color.red(color)   + (255 - Color.red(color))   * factor;
        float g = Color.green(color) + (255 - Color.green(color)) * factor;
        float b = Color.blue(color)  + (255 - Color.blue(color))  * factor;
        return Color.rgb((int) r, (int) g, (int) b);
    }
}

