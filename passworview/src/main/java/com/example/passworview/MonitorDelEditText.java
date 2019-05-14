package com.example.passworview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class MonitorDelEditText extends EditText {
    private static final String TAG = MonitorDelEditText.class.getSimpleName();
    private OnDelKeyEventListener delKeyEventListener;
    private Context context;
    private int textSize=28;

    public MonitorDelEditText(Context context,int textSize,int textColor) {
        super(context);
        this.context=context;
        setViewStyle(textSize,textColor);
    }

    public MonitorDelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorDelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setDefaultViewStyle() {
        setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        setTextSize(textSize);
        setTextColor(context.getResources().getColor(R.color.passwordColor));
        setCursorVisible(false);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    }

    private void setViewStyle(int textSize,int textColor) {
        setDefaultViewStyle();
        if(textSize!=0) {
            this.textSize=textSize;
            setTextSize(textSize);
        }
        if(textColor!=0)
            setTextColor(textColor);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MInputConnection(super.onCreateInputConnection(outAttrs),true);
    }

    private class MInputConnection extends InputConnectionWrapper {

        private MInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN  && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                Log.d(TAG, "mKeyListener -> onKey() , 点击删除按钮");
                if (delKeyEventListener != null) {
                    delKeyEventListener.onDeleteClick();
                    return true;
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float scale = context.getResources().getDisplayMetrics().density;
        int width=(int) (2 * textSize * scale + 0.5f);
        int height=(int) (3 * textSize * scale + 0.5f);
        setMeasuredDimension(width,height);
    }

    public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
        this.delKeyEventListener = delKeyEventListener;
    }

    public interface OnDelKeyEventListener {
        void onDeleteClick();
    }
}

