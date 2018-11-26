package com.example.android.pecode_software;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;

public class MyFragment extends Fragment implements Serializable {

    public static final String NUMBER_OF_FRAGMENT = "MyFragment.NUMBER_OF_FRAGMENT";

    private TextView mPageNumber;
    private ImageView mCreateNotificationIcon;
    private RelativeLayout mDeleteFragment;
    private RelativeLayout mAddFragment;
    private OnFragmentButtonsClickListener mOnFragmentButtonsClickListener;
    private String mFragmentNumber;

    static MyFragment getInstance(String number){
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NUMBER_OF_FRAGMENT, number);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        mFragmentNumber = getArguments().getString(NUMBER_OF_FRAGMENT);
        mPageNumber = view.findViewById(R.id.tv_fragment_number);
        mDeleteFragment = view.findViewById(R.id.delete_fragment);
        mAddFragment = view.findViewById(R.id.add_fragment);
        mCreateNotificationIcon = view.findViewById(R.id.iv_oval_icon);
        if (mFragmentNumber == "1"){
            mDeleteFragment.setVisibility(View.GONE);
        } else {
            mPageNumber.setText(mFragmentNumber);
            mDeleteFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnFragmentButtonsClickListener.deleteFragment();
                }
            });
        }
        mAddFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFragmentButtonsClickListener.addFragment();
            }
        });
        mCreateNotificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFragmentButtonsClickListener.createNotification(Integer.valueOf(mFragmentNumber));
            }
        });
        return view;
    }

    interface OnFragmentButtonsClickListener{
        void deleteFragment();
        void addFragment();
        void createNotification(int number);
    }
    public void setOnFragmentButtonClickListener(OnFragmentButtonsClickListener buttonsClickListener){
        mOnFragmentButtonsClickListener = buttonsClickListener;
    }
}
