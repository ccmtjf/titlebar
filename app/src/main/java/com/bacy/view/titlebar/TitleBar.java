package com.bacy.view.titlebar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.LinkedList;

/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2015/9/25 11:36
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {
    public static final int DEFAULT_DIVIDER_COLOR = android.R.color.darker_gray;
    public static final int DEFAULT_STATUS_COLOR = android.R.color.black;
    //titleBar默认高度：单位像素
    private static final int DEFAULT_MAIN_TEXT_SIZE = 36;
    private static final int DEFAULT_SUB_TEXT_SIZE = 24;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 32;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 88;
    private static final int DEFAULT_ACTION_PADDING = 20;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private Context mContext;
    private TextView mLeftText;
    private TextView mLeftCloseText;//关闭按钮
    private AutoLinearLayout mRightLayout;
    private AutoLinearLayout mCenterLayout;
    private MarqueeTextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    private View mStatusView;//状态栏view

    private boolean mImmersive;

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    private int mActionTextColor;
    private int mHeight;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     * 如果系统版本>=4.4 返回状态栏高度 否则返回0
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME) : 0;
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void init(Context context) {
        mContext = context;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = AutoUtils.getPercentHeightSize(DEFAULT_ACTION_PADDING);
        mOutPadding = AutoUtils.getPercentHeightSize(DEFAULT_ACTION_PADDING);
        mHeight = AutoUtils.getPercentHeightSize(DEFAULT_TITLE_BAR_HEIGHT);
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mLeftCloseText = new TextView(context);
        mCenterLayout = new AutoLinearLayout(context);
        mRightLayout = new AutoLinearLayout(context);
        mDividerView = new View(context);
        mStatusView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(DEFAULT_ACTION_TEXT_SIZE));
        mLeftText.setSingleLine();
        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mOutPadding, 0, mOutPadding, 0);
        setLeftImageResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mLeftCloseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(DEFAULT_ACTION_TEXT_SIZE));
        mLeftCloseText.setSingleLine();
        mLeftCloseText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftCloseText.setPadding(0, 0, mOutPadding, 0);
        setLeftCloseVisible(false);
        setLeftCloseTextColor(R.color.highlighted_text_material_light);

        mCenterText = new MarqueeTextView(context);
        mSubTitleText = new TextView(context);
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(DEFAULT_MAIN_TEXT_SIZE));
        mCenterText.setTextColor(Color.BLACK);
        mCenterText.setGravity(Gravity.CENTER);

        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(DEFAULT_SUB_TEXT_SIZE));
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);
        mCenterLayout.setGravity(Gravity.CENTER);

        mRightLayout.setPadding(mOutPadding, 0, mOutPadding, 0);

        addView(mStatusView, new LayoutParams(LayoutParams.MATCH_PARENT, mStatusBarHeight));
        addView(mLeftText, layoutParams);
        addView(mLeftCloseText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        setDividerColor(getResources().getColor(DEFAULT_DIVIDER_COLOR));
        mStatusView.setBackgroundColor(getResources().getColor(DEFAULT_STATUS_COLOR));
    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = AutoUtils.getPercentHeightSize(height);
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public void setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
    }

    /**
     * 设置左侧关闭按钮图标
     *
     * @param resId
     */
    public void setLeftCloseImageResource(int resId) {
        mLeftCloseText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        mLeftCloseText.setText("");
    }

    /**
     * 设置左侧关闭按钮监听
     *
     * @param l
     */
    public void setLeftCloseClickListener(OnClickListener l) {
        mLeftCloseText.setOnClickListener(l);
    }

    public void setLeftText(CharSequence title) {
        mLeftText.setText(title);
    }

    public void setLeftText(int resid) {
        mLeftText.setText(resid);
    }

    /**
     * 设置左侧关闭按钮文本
     *
     * @param title
     */
    public void setLeftCloseText(CharSequence title) {
        mLeftCloseText.setText(title);
    }

    public void setLeftCloseText(int resid) {
        mLeftCloseText.setText(resid);
    }

    public void setLeftTextSize(float size) {
        mLeftText.setTextSize(size);
    }

    public void setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
    }

    /**
     * 设置关闭按钮颜色
     *
     * @param color
     */
    public void setLeftCloseTextColor(int color) {
        mLeftCloseText.setTextColor(getResources().getColorStateList(color));
    }

    public void setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取左侧关闭按钮是否显示
     *
     * @return One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    public int getLeftCloseVisible() {
        return mLeftCloseText.getVisibility();
    }

    /**
     * 设置左侧关闭按钮是否显示
     *
     * @param visible
     */
    public void setLeftCloseVisible(boolean visible) {
        mLeftCloseText.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            mLeftCloseText.setText("关闭");
        }
    }

    public void setTitle(CharSequence title) {
        if (title == null) {
            //如果为空 直接返回
            return;
        }
        if (!TextUtils.isEmpty(title) && title.length() > 18) {
            setTitleSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(28));
        } else if (title.length() > 14) {
            setTitleSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(32));
        }
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitleColor(int resid) {
        mCenterText.setTextColor(resid);
    }

    public void setTitleSize(float size) {
        mCenterText.setTextSize(size);
    }

    /**
     * 大小类型
     *
     * @param type
     * @param size
     */
    public void setTitleSize(int type, float size) {
        mCenterText.setTextSize(type, size);
    }

    public void setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
    }

    public void setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
    }

    public void setSubTitleSize(float size) {
        mSubTitleText.setTextSize(size);
    }

    public void setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    /**
     * 设置titleBar是否有分割线
     *
     * @param isVisible
     */
    public void setDividerVisible(boolean isVisible) {
        if (isVisible) {
            mDividerView.setVisibility(VISIBLE);
        } else {
            mDividerView.setVisibility(GONE);
        }

    }

    public void setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
    }

    /**
     * 设置状态栏的颜色
     *
     * @param color
     */
    public void setStatusColor(int color) {
        mStatusView.setBackgroundColor(color);
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     * @param rId        背景资源id
     */
    public void addActions(ActionList actionList, int rId) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i), rId);
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     * @param rId    背景资源id
     */
    public View addAction(Action action, int rId) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index, rId);
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index, 0);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index, int rId) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);

        //判断当前action是否存在
        if (getViewById(action.getTagId()) != null)
            return getViewByAction(action);
        View view = inflateAction(action);
        //设置背景
        view.setBackgroundResource(rId);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view = null;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(DEFAULT_ACTION_TEXT_SIZE));
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }
        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        //添加id
        view.setId(action.getTagId());
        view.setOnClickListener(this);
        return view;
    }

    /**
     * 通过action 获取action view
     *
     * @param action
     * @return
     */
    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    /**
     * 通过action 获取action view
     *
     * @param id actioin 唯一id
     * @return
     */
    public View getViewById(int id) {
        View view = findViewById(id);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mLeftCloseText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() + mLeftCloseText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - (mLeftText.getMeasuredWidth() + mLeftCloseText.getMeasuredWidth()), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mStatusView.layout(0, 0, mScreenWidth, mStatusBarHeight);//状态栏view
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mLeftCloseText.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight, mLeftText.getMeasuredWidth() + mLeftCloseText.getMeasuredWidth(), mLeftCloseText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() + mLeftCloseText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth() + mLeftCloseText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth() - mLeftCloseText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }


    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();

        int getDrawable();

        /**
         * 获取find view Tag Id
         *
         * @return
         */
        int getTagId();

        void performAction(View view);
    }

    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;
        private int mId;

        public ImageAction(int drawable, int mId) {
            mDrawable = drawable;
            this.mId = mId;
        }

        @Override
        public int getTagId() {
            return mId;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;
        private int mId;

        public TextAction(String text, int mId) {
            mText = text;
            this.mId = mId;
        }

        @Override
        public int getTagId() {
            return mId;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }

}
