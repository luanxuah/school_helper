package com.luanxu.custom.bottommenu;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LiangYX
 * @ClassName: BottomButtonMenu
 * @date: 2016年4月27日 下午5:18:13
 * @Description: 底部按钮菜单
 */
public class BottomButtonMenu extends BottomMenuWindow {

	private List<Button> dispButtons = new ArrayList<Button>();

	public BottomButtonMenu(Activity activity) {
		super(activity);
		setContentView(R.layout.bottom_menu_pick_photo);
		getContentView().findViewById(R.id.btn_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}

	/**
	 * @Description: 普通按钮1
	 * @param listener
	 * @param textContent
	 * @return: void
	 */
	public void addButtonFirst(final MenuClickedListener listener, String textContent) {
		Button button = ((Button) getContentView().findViewById(R.id.button1));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}
	/** 创建指定颜色的按钮1 （重载方法）**/
	public void addButtonFirst(final MenuClickedListener listener, String textContent, int textColor) {
		Button button = ((Button) getContentView().findViewById(R.id.button1));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		button.setTextColor(textColor);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}
	
	/** 创建只展示文字不能点击的按钮1 （重载方法）**/
	public void addButtonFirst(final MenuClickedListener listener, String textContent, boolean isClickable, int textColor) {
		Button button = ((Button) getContentView().findViewById(R.id.button1));
		button.setText(textContent);
		button.setTextColor(textColor);
		button.setVisibility(View.VISIBLE);
		button.setClickable(isClickable);
		dispButtons.add(button);
	}
	
	/**
	 * @Description: 普通按钮2
	 * @param listener
	 * @param textContent
	 * @return: void
	 */
	public void addButtonSecond(final MenuClickedListener listener, String textContent) {
		Button button = ((Button) getContentView().findViewById(R.id.button2));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}
	
	/** 创建指定颜色的按钮2 （重载方法）**/
	public void addButtonSecond(final MenuClickedListener listener, String textContent, int textColor) {
		Button button = ((Button) getContentView().findViewById(R.id.button2));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		button.setTextColor(textColor);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}
	
	/**
	 * @Description: 普通按钮3
	 * @param listener
	 * @param textContent
	 * @return: void
	 */
	public void addButtonThird(final MenuClickedListener listener, String textContent) {
		Button button = ((Button) getContentView().findViewById(R.id.button3));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	/**
	 * @Description: 普通按钮4
	 * @param listener
	 * @param textContent
	 * @return: void
	 */
	public void addButtonFourth(final MenuClickedListener listener, String textContent) {
		Button button = ((Button) getContentView().findViewById(R.id.button4));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	/**
	 * @Description: 红色按钮
	 * @param listener
	 * @param textContent
	 * @return: void
	 */
	public void addButtonChangeNumber(final MenuClickedListener listener, String textContent) {
		Button button = ((Button) getContentView().findViewById(R.id.btn_exit));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	public void show() {
		if (dispButtons.size() > 0) {
			for (int i = 0; i < dispButtons.size(); i++) {
				Button dispButton = dispButtons.get(i);
				if (i == 0) {
					if (dispButtons.size() == 1) {// 只有一个按钮，显示上下圆角按钮
						dispButton.setBackgroundResource(R.drawable.selector_shape_white_8px);
					} else { // 多于一个按钮，显示上圆角按钮
						dispButton.setBackgroundResource(R.drawable.selector_shape_top_ffffff_8px);
						View lineView = getContentView().findViewWithTag("line" + (String) dispButton.getTag());
						if (lineView != null) {
							lineView.setVisibility(View.VISIBLE);
						}
					}
				} else {
					if (i < dispButtons.size() - 1) {// 中间按钮
						dispButton.setBackgroundResource(R.drawable.selector_shape_ffffff_0dp);
						View lineView = getContentView().findViewWithTag("line" + (String) dispButton.getTag());
						if (lineView != null) {
							lineView.setVisibility(View.VISIBLE);
						}
					} else {// 下圆角按钮
						dispButton.setBackgroundResource(R.drawable.selector_shape_bottom_ffffff_8px);
					}
				}
			}
		}
		super.show();
	}

	public void setSelected(int res) {
		getContentView().findViewById(res).setPressed(true);
	}
}
