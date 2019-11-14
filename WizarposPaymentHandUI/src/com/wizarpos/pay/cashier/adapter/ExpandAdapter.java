package com.wizarpos.pay.cashier.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wizarpos.pay2.lite.R;

public class ExpandAdapter extends BaseExpandableListAdapter implements
		ExpandableListAdapter {

	private LayoutInflater vi;
	private String[][] data;
	private String[][] listinfo;
	private String[] groupname;
	private int[] ImgBckgrnd;
	private Context context;
	BounceInterpolator bounceInterpolator;
	View v;

	private static final int GROUP_ITEM_RESOURCE = R.layout.group_transaction_records;
	private static final int CHILD_ITEM_RESOURCE = R.layout.item_transaction_records;

	public ExpandAdapter(Context context, Activity activity,
						 String[] groupname, int[] ImgBckgrnd, String[][] listinfo,
						 String[][] data) {
		this.context = context;
		this.groupname = groupname;
		this.ImgBckgrnd = ImgBckgrnd;
		this.listinfo = listinfo;
		this.data = data;

		vi = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		bounceInterpolator = new BounceInterpolator();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String child = getChild(groupPosition, childPosition);
		String list = getList(groupPosition, childPosition);
		v = convertView;
		v = vi.inflate(CHILD_ITEM_RESOURCE, null);
		final ViewHolder holder = new ViewHolder(v);

		holder.rlItemContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.llDetail.setVisibility(View.VISIBLE);
			}
		});
    //TODO
//		if (child != null) {
//
//			holder.ExpCol.setFocusable(false);
//
//			/**
//			 * TO SET CHILDLIST HEAD AND DETAIL *
//			 * */
//
//			holder.ListHead.setText(Html.fromHtml(child));
//			holder.ListDetail.setText(Html.fromHtml(list));
//
//			/**
//			 * EXPAND AND COLAPSE SECOND LEVEL CHILD LAYOUT WITH BOUNCE
//			 * ANIMATION ; DEFINE SLIDEUP AND SLIDEDOWN ANIMATION PROPERTIES
//			 * (SEE res/anim FOLDER) ; *PLAY WITH LAYOUT VISIBILITY PROPERTY ;
//			 **/
//
//			final Animation slidedown = AnimationUtils.loadAnimation(
//					v.getContext(), R.anim.slide_down);
//			final Animation slideup = AnimationUtils.loadAnimation(
//					v.getContext(), R.anim.slide_up);
//
//			/** SET BOUNCE INTERPOLATOR TO SLIDEDOWN **/
//			slidedown.setInterpolator(bounceInterpolator);
//
//			slideup.setAnimationListener(new AnimationListener() {
//
//				@Override
//				public void onAnimationStart(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					holder.toggleLayout.setVisibility(View.GONE);
//				}
//			});
//
//			/**
//			 * EXPANDING AND COLAPSING SECOND LEVEL CHILD
//			 * **/
//			holder.ExpCol
//					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(CompoundButton buttonView,
//								boolean isChecked) {
//							// TODO Auto-generated method stub
//							if (holder.ExpCol.isChecked()) {
//								holder.toggleLayout.startAnimation(slidedown);
//								holder.toggleLayout.setVisibility(View.VISIBLE);
//							} else {
//								holder.toggleLayout.startAnimation(slideup);
//							}
//						}
//
//					});
//
//			/**
//			 * ON CLICK LISTENER FOR CHILD
//			 * **/
//			holder.ChildLayout.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (holder.ExpCol.isChecked()) {
//						holder.ExpCol.setChecked(false);
//					} else {
//						holder.ExpCol.setChecked(true);
//					}
//
//				}
//			});
//
//			/**
//			 * ONCLICK METHODS FOR SECOND LEVEL CHILD BUTTONS
//			 * **/
//			holder.directions.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					// DO SOMETHING
//
//					Toast.makeText(context, "GET DIRECTIONS",
//							Toast.LENGTH_SHORT).show();
//
//				}
//
//			});
//
//			holder.details.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					// DO SOMETHING
//
//					Toast.makeText(context, "GET DETAILS", Toast.LENGTH_SHORT)
//							.show();
//				}
//
//			});
//
//		}

		return v;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			v = vi.inflate(GROUP_ITEM_RESOURCE, null);
			holder = new ViewHolder(v);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (getGroupName(groupPosition) != null) {

			/** SET GROUP HEAD TEXT **/
//			holder.GroupHead.setText(getGroupName(groupPosition));

			/**
			 * SET IMAGE BACKGROUND DO NOT LOAD IMAGES ON UI THREAD USE
			 * ASYNCTASK TO LOAD IMAGES FROM WEB
			 **/

//			holder.LayoutBackground
//					.setBackgroundResource(getImage(groupPosition));
		}
		return v;
	}

	public class ViewHolder {

		public RelativeLayout rlItemContent;
		public LinearLayout llDetail;

		public ToggleButton ExpCol;
		public RelativeLayout toggleLayout;
		public RelativeLayout ChildLayout;
		public ImageView LayoutBackground;
		TextView GroupHead;
		TextView ListHead;
		TextView ListDetail;
		Button directions;
		Button details;

		public String GroupName;
		public String ChildName;
		public String list_info;

		public ViewHolder(View v) {
//			this.LayoutBackground = (ImageView)v.findViewById(R.id.listbackground);
//			this.ChildLayout = (RelativeLayout)v.findViewById(R.id.list_Item_layout);
//			this.directions = (Button)v.findViewById(R.id.directions);
//			this.details = (Button)v.findViewById(R.id.details);
//			this.GroupHead = (TextView)v.findViewById(R.id.lblListHeader);
//			this.ListHead = (TextView)v.findViewById(R.id.lblListItem);
//			this.ListDetail = (TextView)v.findViewById(R.id.listItemInfo);
//			this.toggleLayout = (RelativeLayout)v.findViewById(R.id.toggle_layout);
//			this.ExpCol=(ToggleButton)v.findViewById(R.id.expand_colapse);
			this.rlItemContent = (RelativeLayout) v.findViewById(R.id.rlItemContent);
			this.llDetail = (LinearLayout) v.findViewById(R.id.llDetail);
		}


	}

	public int getImage(int groupPosition) {
		return ImgBckgrnd[groupPosition];
	}

	public String getGroupName(int groupPosition) {
		return groupname[groupPosition];
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return data[groupPosition][childPosition];
	}

	public String getList(int groupPosition, int childPosition) {
		return listinfo[groupPosition][childPosition];

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data[groupPosition].length;
	}

	public int getGroupCount() {
		return groupname.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getGroup(int groupPosition) {
		return "group-" + groupPosition;
	}

}
