package com.luanxu.custom;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;

import java.util.Stack;

public class CommonDialog extends Dialog {

    private View contentView;
    private View selfDifineView;
    private Dialog dialog;
    public static String YXCK_STYLE = "yxck_style";
    public static String WEIXIN_STYLE = "weixin_style";

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private Context context;
    private SparseArray<Button> btns = null;
    private int SureColor;

    public CommonDialog(Context context, String style) {
        super(context);
        this.context = context;
        btns = new SparseArray<Button>();
        dialog = new Dialog(context, R.style.CustomProgressDialog);
        if (WEIXIN_STYLE.equalsIgnoreCase(style)) {
            contentView = LayoutInflater.from(context).inflate(R.layout.common_dialog_layout_wenxin, null);
        } else {
            contentView = LayoutInflater.from(context).inflate(R.layout.common_dialog_layout, null);
        }
        init();
    }

    public CommonDialog(Context context) {
        super(context);
        this.context = context;
        btns = new SparseArray<Button>();
        dialog = new Dialog(context, R.style.CustomProgressDialog);
        contentView = LayoutInflater.from(context).inflate(R.layout.common_dialog_layout, null);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.addContentView(contentView, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 设置窗口大小和位置
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = (int) (CommonUtils.getDeviceSize(context).x * 0.8);
        dialog.onWindowAttributesChanged(wl);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public View getContentView() {
        return contentView;
    }

    public void setCancelable(boolean cancelable) {
        if (dialog != null) {
            dialog.setCancelable(cancelable);
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setTitle(int id) {
        if (id < 0) {
            contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
            titleTv.setText(id);
        }
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }
    }

    public void setTitle(String title, boolean isShow) {
        if (TextUtils.isEmpty(title)) {
            contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
            if (isShow) {
                titleTv.setVisibility(View.VISIBLE);
            }
            titleTv.setText(title);
        }
    }

    public void setTitle(String title, int gravity) {
        if (TextUtils.isEmpty(title)) {
            contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
            titleTv.setText(title);
            titleTv.setGravity(gravity);
        }
    }

    public void setMessage(String title, int gravity) {
        contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        contentView.findViewById(R.id.tv_go).setVisibility(View.VISIBLE);
        TextView titleTv = (TextView) contentView.findViewById(R.id.tv_message);
        titleTv.setText(title);
        titleTv.setGravity(gravity);
    }

    public void setMiddleBtn(final BtnClickedListener btnOk, String text) {

        Button middleBtn = (Button) contentView.findViewById(R.id.middle_btn);
        contentView.findViewById(R.id.line_middle).setVisibility(View.VISIBLE);
        middleBtn.setVisibility(View.VISIBLE);
        middleBtn.setText(text);
        btns.put(2, middleBtn);
        middleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (btnOk != null) {
                    btnOk.onBtnClicked();
                }
            }
        });
    }

    public void setMessage(int id) {
        if (id < 0) {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.GONE);
        } else {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.VISIBLE);
            ((TextView) contentView.findViewById(R.id.tv_message)).setText(id);
        }
    }

    public void setMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.GONE);
        } else {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.VISIBLE);
            ((TextView) contentView.findViewById(R.id.tv_message)).setText(msg);
        }
    }

    public void setMessage(String msg, boolean isHtml) {
        if (TextUtils.isEmpty(msg)) {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.GONE);
        } else {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.VISIBLE);
            if (isHtml){
                ((TextView) contentView.findViewById(R.id.tv_message)).setText(Html.fromHtml(msg));
            }else {
                ((TextView) contentView.findViewById(R.id.tv_message)).setText(msg);
            }

        }
    }

    public void setPositiveButton(final BtnClickedListener btnOk, String text) {
        Button sureBtn = (Button) contentView.findViewById(R.id.confirm_btn);
        sureBtn.setVisibility(View.VISIBLE);
        sureBtn.setText(text);
        btns.put(1, sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (btnOk != null) {
                    btnOk.onBtnClicked();
                }
            }
        });
    }

    public void setPositiveButton(final BtnClickedListener btnOk, String text, int sureColor) {
        Button sureBtn = (Button) contentView.findViewById(R.id.confirm_btn);
        sureBtn.setTextColor(sureColor);
        setPositiveButton(btnOk, text);
    }

    public void setPositiveButtonOpen(final BtnClickedListener btnOk, String text) {

        Button sureBtn = (Button) contentView.findViewById(R.id.confirm_btn);
        sureBtn.setVisibility(View.VISIBLE);
        sureBtn.setText(text);
        btns.put(1, sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOk != null) {
                    btnOk.onBtnClicked();
                }
            }
        });
    }

    public void setPositiveButton(final BtnClickedListener btnOk, int id) {
        setPositiveButton(btnOk, context.getResources().getString(id));
    }

    public void setCancleButton(final BtnClickedListener cancleOk, String text) {
        Button canBtn = (Button) contentView.findViewById(R.id.cancel_btn);
        canBtn.setVisibility(View.VISIBLE);
        canBtn.setText(text);
        btns.put(0, canBtn);
        canBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (cancleOk != null) {
                    cancleOk.onBtnClicked();
                }
            }
        });
    }

    public void setCancleButton(final BtnClickedListener cancleOk, int id) {
        setCancleButton(cancleOk, context.getResources().getString(id));
    }

    public void addView(View view) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView.findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = view;
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addLt.addView(selfDifineView, params);
        }
    }

    // 添加自定义的 布局
    public void addView(int viewId) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView.findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = LayoutInflater.from(context).inflate(viewId, null);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addLt.addView(selfDifineView, params);
        }
    }

    public View getSelfDifineView() {
        return selfDifineView;
    }

    public void showDialog() {
        // 当前是否有对话框正在显示，如果有后续对话框不弹出
        if (btns.size() > 0) {
            if (btns.size() == 2) {
                getContentView().findViewById(R.id.line3).setVisibility(View.VISIBLE);
            } else if (btns.size() == 1) {
                Button cancelBtn = btns.get(0);
                if (cancelBtn != null) {
                    cancelBtn.setBackgroundResource(R.drawable.bg_common_dialog_btn);
                }
            }
        }
        getDialog().show();
    }

    public void hideLine() {
        getContentView().findViewById(R.id.line3).setVisibility(View.GONE);
    }

    public void hideBottom() {
        getContentView().findViewById(R.id.operation_layout).setVisibility(View.GONE);
    }

    public interface BtnClickedListener {
        public void onBtnClicked();
    }

    public void setOnCancelListener(OnCancelListener listener) {
        if (dialog != null) {
            dialog.setOnCancelListener(listener);
        }
    }

    public class ActivityDialogStack {
        // 当前显示对话框的类型
        public int disDialogType = -1;
        // 对话框栈
        public Stack<CommonDialog> dialogStack = new Stack<CommonDialog>();
        // 对话框的类型�?
        public Stack<String> typeStack = new Stack<String>();
    }
}
