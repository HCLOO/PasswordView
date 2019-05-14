package com.example.passworview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PasswordView extends LinearLayout {

    private Context context;
    private int num;
    private String title;
    private int defaultTitleSize=16;
    private int titleSize;
    private int titleColor;
    private int textSize;
    private int textColor;
    private MonitorDelEditText[] viewArray;
    private LinearLayout LLEidt;
    private TextView TVTitle;

    public PasswordView(Context context) {
        super(context);
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MLinearyView);
        try {
            num=typedArray.getInt(R.styleable.MLinearyView_Num,0);
            title=typedArray.getString(R.styleable.MLinearyView_Title);
            titleSize=typedArray.getDimensionPixelSize(R.styleable.MLinearyView_titleSize,0);
            float fontScale1 = context.getResources().getDisplayMetrics().scaledDensity;
            titleSize=(int) (titleSize / fontScale1 + 0.5f);
            titleColor=typedArray.getColor(R.styleable.MLinearyView_titleColor,0);
            textSize=typedArray.getDimensionPixelSize(R.styleable.MLinearyView_textSize,0);
            float fontScale2 = context.getResources().getDisplayMetrics().scaledDensity;
            textSize=(int) (textSize / fontScale2 + 0.5f);
            textColor=typedArray.getColor(R.styleable.MLinearyView_textColor,0);
        } finally {
            typedArray.recycle();
        }
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        viewArray=new MonitorDelEditText[num];
        TVTitle=new TextView(context);
        TVTitle.setText(title);
        TVTitle.setTextSize(defaultTitleSize);
        TVTitle.setTextColor(context.getResources().getColor(R.color.titleColor));
        if (titleSize!=0)
            TVTitle.setTextSize(titleSize);
        if (titleColor!=0)
            TVTitle.setTextColor(titleColor);
        TVTitle.setGravity(Gravity.CENTER);
        LLEidt=new LinearLayout(context);
        LLEidt.setOrientation(HORIZONTAL);
        for(int i=0;i<num;++i) {
            viewArray[i]=new MonitorDelEditText(context,textSize,textColor);
            viewArray[i].addTextChangedListener(mTextWatcher);
            viewArray[i].setDelKeyEventListener(mKeyListener);
            LLEidt.addView(viewArray[i]);
        }
        TextView nullView=new TextView(context);
        nullView.setText(" ");
        nullView.setTextSize(5);
        nullView.setVisibility(INVISIBLE);
        addView(nullView);
        addView(TVTitle);
        addView(LLEidt);
        resetPasswordEditTexts();
    }

    /**
     * 监听密码文本框是否输入数据
     * */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //判断输入的字符是否合法，不合法，就清除当前文本框
            if (s != null && !isPasswordLegal(s.toString())) {
                s.clear();
            } else if (s != null && s.toString() != null && s.toString().length() > 0) {//输入字符合法，就自动切换文本框
                for (int i=0;i<viewArray.length;++i) {
                    if (viewArray[i] != null && viewArray[i].isFocused()) {
                        if (i==viewArray.length-1) {
                            String password = getPassword();
                            Log.d( "input EditText" , password);
                            break;
                        } else {
                            if (viewArray[i+1] != null) {
                                viewArray[i+1] .setEnabled(true);
                                viewArray[i+1] .requestFocus();
                            }
                            viewArray[i] .clearFocus();
                            viewArray[i].setEnabled(false);
                            break;
                        }
                    }
                }
            }
        }
    };

    /**
     * 监听软键盘的删除按钮
     * */
    private MonitorDelEditText.OnDelKeyEventListener mKeyListener = new MonitorDelEditText.OnDelKeyEventListener() {
        @Override
        public void onDeleteClick() {
            //点击删除按钮时，逐个删除密码字符
            for (int i=viewArray.length-1;i>=0;--i) {
                if(i==viewArray.length-1) {
                    if(viewArray[viewArray.length-1] != null && viewArray[viewArray.length-1].getText() != null &&
                            !TextUtils.isEmpty(viewArray[viewArray.length-1].getText().toString())){
                        Log.d( "delete EditText" , viewArray[viewArray.length-1].getText().toString());
                        viewArray[viewArray.length-1].setText("");
                        break;
                    }
                } else {
                    if (viewArray[i+1] != null && viewArray[i+1].isFocused()) {
                        if(viewArray[i] != null) {
                            viewArray[i].setEnabled(true);
                            viewArray[i].requestFocus();
                            viewArray[i].setText("");
                        }
                        viewArray[i+1].clearFocus();
                        viewArray[i+1].setEnabled(false);
                        break;
                    }
                }
            }
        }
    };

    /**
     * 获取密码框中输入的密码
     * @return 输入的全部密码
     * */
    private String getPassword(){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<viewArray.length;++i)
            if (viewArray[i] != null && viewArray[i].getText() != null &&
                    !TextUtils.isEmpty(viewArray[i].getText().toString())) {
                builder.append(viewArray[i].getText().toString());
            }
        return builder.toString();
    }

    /**
     * 重置密码框
     * */
    public void resetPasswordEditTexts(){
        //清除密码
        for (int i=0;i<viewArray.length;++i)
            viewArray[i].setText("");
        //重置焦点
        viewArray[0].setEnabled(true);
        viewArray[0].requestFocus();
        for (int i=1;i<viewArray.length;++i)
            viewArray[i].setEnabled(false);
    }

    /**
     * 判断输入的密码字符是否合法
     * @param str 输入的密码字符
     * @return true：合法；false：不合法
     * */
    private boolean isPasswordLegal(String str){
        Log.d("isPasswordLegal()" ,str);
        String pswType = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(!TextUtils.isEmpty(str) && pswType.contains(str)){
            return true;
        }else{
            return false;
        }
    }
}
