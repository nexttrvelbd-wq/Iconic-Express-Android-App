package com.iconicexpress.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://iconicexpressbd.com/";
    private static final String PHONE = "01715668016";
    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout root;
    private int purple = Color.rgb(169, 0, 214);
    private int sky = Color.rgb(22, 181, 229);
    private int dark = Color.rgb(28, 18, 36);

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSplash();
        new Handler().postDelayed(this::showApp, 1450);
    }

    private void showSplash() {
        LinearLayout splash = new LinearLayout(this);
        splash.setOrientation(LinearLayout.VERTICAL);
        splash.setGravity(Gravity.CENTER);
        splash.setBackgroundColor(Color.WHITE);

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.iconicexpress.app.R.drawable.iconic_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        splash.addView(logo, new LinearLayout.LayoutParams(-1, dp(190)));

        TextView tagline = new TextView(this);
        tagline.setText("Premium Travel • Easy Booking • Trusted Service");
        tagline.setTextColor(Color.DKGRAY);
        tagline.setTextSize(13);
        tagline.setGravity(Gravity.CENTER);
        splash.addView(tagline, new LinearLayout.LayoutParams(-1, dp(40)));

        ProgressBar p = new ProgressBar(this);
        p.setIndeterminate(true);
        splash.addView(p, new LinearLayout.LayoutParams(dp(34), dp(34)));
        setContentView(splash);
    }

    private void showApp() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(247,248,252));

        root.addView(buildTopBar(), new LinearLayout.LayoutParams(-1, dp(72)));

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        root.addView(progressBar, new LinearLayout.LayoutParams(-1, dp(3)));

        webView = new WebView(this);
        configureWebView();
        root.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1));

        root.addView(buildBottomNav(), new LinearLayout.LayoutParams(-1, dp(70)));
        setContentView(root);
        webView.loadUrl(HOME_URL);
    }

    private View buildTopBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(14), dp(8), dp(12), dp(8));
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{purple, Color.rgb(127, 0, 188), sky});
        bar.setBackground(bg);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.iconic_logo);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(54), dp(54));
        bar.addView(logo, lp);

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(10), 0, 0, 0);
        TextView title = label("ICONIC EXPRESS", 18, Color.WHITE, true);
        TextView sub = label("Iconic services with a smile", 11, Color.WHITE, false);
        titles.addView(title);
        titles.addView(sub);
        bar.addView(titles, new LinearLayout.LayoutParams(0, -2, 1));

        TextView menu = label("⋮", 30, Color.WHITE, true);
        menu.setGravity(Gravity.CENTER);
        menu.setOnClickListener(v -> showMenu(v));
        bar.addView(menu, new LinearLayout.LayoutParams(dp(44), dp(54)));
        return bar;
    }

    private View buildBottomNav() {
        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(6), dp(6), dp(6), dp(8));
        nav.setBackgroundColor(Color.WHITE);
        nav.setElevation(dp(12));

        nav.addView(navButton("⌂", "Home", v -> loadHome()), weightParams());
        nav.addView(navButton("☰", "Menu", v -> showMenu(v)), weightParams());
        nav.addView(navButton("☎", "Call", v -> callNow()), weightParams());
        nav.addView(navButton("🎫", "Booking", v -> loadHome()), weightParams());
        return nav;
    }

    private LinearLayout.LayoutParams weightParams() {
        return new LinearLayout.LayoutParams(0, -1, 1f);
    }

    private View navButton(String icon, String text, View.OnClickListener listener) {
        LinearLayout b = new LinearLayout(this);
        b.setOrientation(LinearLayout.VERTICAL);
        b.setGravity(Gravity.CENTER);
        TextView i = label(icon, 22, purple, true);
        TextView t = label(text, 11, dark, true);
        b.addView(i, new LinearLayout.LayoutParams(-1, dp(30)));
        b.addView(t, new LinearLayout.LayoutParams(-1, dp(20)));
        b.setOnClickListener(listener);
        return b;
    }

    private void configureWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setLoadWithOverviewMode(false);
        s.setUseWideViewPort(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(request.getUrl().toString());
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(url);
            }
        });
        webView.setOnLongClickListener(v -> false);
        webView.setBackgroundColor(Color.WHITE);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private boolean handleUrl(String url) {
        if (url.startsWith("tel:")) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
            return true;
        }
        if (url.startsWith("mailto:") || url.startsWith("sms:") || url.startsWith("whatsapp:")) {
            try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); } catch (Exception ignored) {}
            return true;
        }
        return false;
    }

    private void loadHome() {
        if (webView != null) webView.loadUrl(HOME_URL);
    }

    private void callNow() {
        try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE)));
        } catch (Exception e) {
            Toast.makeText(this, "Call service unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMenu(View anchor) {
        final android.app.Dialog dialog = new android.app.Dialog(this);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(22), dp(18), dp(22), dp(18));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(dp(24));
        box.setBackground(bg);

        TextView head = label("ICONIC EXPRESS", 20, purple, true);
        box.addView(head, new LinearLayout.LayoutParams(-1, dp(38)));
        TextView line = label("Premium Travel • Trusted Service", 12, Color.GRAY, false);
        box.addView(line, new LinearLayout.LayoutParams(-1, dp(28)));

        addMenuItem(box, "⌂  Home", v -> { dialog.dismiss(); loadHome(); });
        addMenuItem(box, "🎫  Online Ticket Booking", v -> { dialog.dismiss(); loadHome(); });
        addMenuItem(box, "☎  Call For Ticket: 01715-668016", v -> { dialog.dismiss(); callNow(); });
        addMenuItem(box, "↻  Refresh Website", v -> { dialog.dismiss(); if (webView != null) webView.reload(); });
        addMenuItem(box, "ℹ  About Iconic Express", v -> Toast.makeText(this, "Iconic Express • iconicexpressbd.com", Toast.LENGTH_LONG).show());

        dialog.setContentView(box);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout((int)(getResources().getDisplayMetrics().widthPixels * .90), -2);
        }
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int)(getResources().getDisplayMetrics().widthPixels * .90), -2);
        }
    }

    private void addMenuItem(LinearLayout box, String text, View.OnClickListener listener) {
        TextView item = label(text, 15, dark, true);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(4), 0, 0, 0);
        item.setOnClickListener(listener);
        box.addView(item, new LinearLayout.LayoutParams(-1, dp(52)));
    }

    private TextView label(String text, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        return t;
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    private int dp(int v) { return Math.round(v * getResources().getDisplayMetrics().density); }
}
