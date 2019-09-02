package com.beyond.popscience.physicalstore.fragments;



import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.physicalstore.ShopMainActivity;
import com.beyond.popscience.physicalstore.adapters.FoodAdapter;
import com.beyond.popscience.physicalstore.adapters.TypeAdapter;
import com.beyond.popscience.physicalstore.view.ListContainer;


public class FirstFragment extends BaseFragment {

	private ListContainer listContainer;



	public FoodAdapter getFoodAdapter() {
		return listContainer.foodAdapter;
	}

	public TypeAdapter getTypeAdapter() {
		return listContainer.typeAdapter;
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_first;
	}

	@Override
	public void initUI() {
		super.initUI();
		listContainer = (ListContainer)mRootView. findViewById(R.id.listcontainer);
		listContainer.setAddClick((ShopMainActivity) getActivity());
	}
}
