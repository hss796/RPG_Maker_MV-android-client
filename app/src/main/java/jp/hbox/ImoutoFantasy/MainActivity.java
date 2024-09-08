package jp.hbox.ImoutoFantasy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.getcapacitor.BridgeActivity;
import android.view.WindowManager;

public class MainActivity extends BridgeActivity {

    private AlertDialog mQuitDialog;
    private int mSystemUiVisibility;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置屏幕保持唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 1 分钟后允许设备休眠
        new Handler().postDelayed(() -> {
            // 移除保持唤醒的标志，允许设备进入休眠
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }, 60000); // 60000 毫秒 = 1 分钟

        if (BuildConfig.BACK_BUTTON_QUITS) {
            createQuitDialog(); // 创建退出对话框
        }

        updateSystemUiVisibility(); // 更新系统 UI 可见性
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.BACK_BUTTON_QUITS && mQuitDialog != null) {
            mQuitDialog.show(); // 显示退出对话框
        } else {
            super.onBackPressed(); // 默认返回操作
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSystemUiVisibility(); // 恢复系统 UI 可见性
    }

    private void updateSystemUiVisibility() {
        mSystemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSystemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(mSystemUiVisibility);
    }

    private void createQuitDialog() {
        String appName = getString(R.string.app_name);
        String[] quitLines = getResources().getStringArray(R.array.quit_message);
        SpannableStringBuilder quitMessage = new SpannableStringBuilder();

        for (int ii = 0; ii < quitLines.length; ii++) {
            String line = quitLines[ii].replace("$1", appName);
            quitMessage.append(line);
            if (ii < quitLines.length - 1) {
                quitMessage.append("\n");
            }
        }

        // 创建一个 TextView，用于在对话框中显示带有渐变色的应用名称
        TextView textView = new TextView(this);
        textView.setText(quitMessage);
        textView.setTextSize(16); // 可以根据需求调整字体大小
        textView.setPadding(50, 30, 50, 30); // 设置合适的 padding
        textView.setTextColor(Color.BLACK); // 设置默认文本颜色，防止渐变失效时不显示文本

        // 使用 ViewTreeObserver 确保 TextView 尺寸计算完成后应用渐变效果
        textView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            // 在 TextView 渲染完成后应用渐变色
            Shader textShader = new LinearGradient(0, 0, textView.getWidth(), 0,
                    new int[]{Color.RED, Color.BLUE}, // 渐变色从红色到蓝色
                    null, Shader.TileMode.CLAMP);
            textView.getPaint().setShader(textShader);
        });

        // 创建并显示退出对话框
        if (quitMessage.length() > 0) {
            mQuitDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
                    .setView(textView)  // 将自定义 TextView 作为对话框的消息视图
                    .setPositiveButton("确定", (dialog, which) -> MainActivity.super.onBackPressed())
                    .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                    .setOnDismissListener(dialog -> updateSystemUiVisibility())
                    .create();
        }
    }
}

