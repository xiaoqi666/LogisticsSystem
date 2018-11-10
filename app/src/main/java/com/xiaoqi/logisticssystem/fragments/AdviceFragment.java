package com.xiaoqi.logisticssystem.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.AdviceBean;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AdviceFragment extends Fragment {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_advice, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btn_commit)
    void onClick() {
        String content = etContent.getText().toString();
        String title = etTitle.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String dateStr = sdf.format(new Date());
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(title)) {
            AppClient.showToast("内容或标题不能为空");
            // AppClient.showToast("提交成功");
            return;
        }
        AdviceBean adviceBean = new AdviceBean();
        adviceBean.setContent(content);
        adviceBean.setTitle(title);
        adviceBean.setDateStr(dateStr);
        adviceBean.setUserId(SpUtils.getString("userId", ""));
        AppClient.showToast("提交成功");
        AppClient.getDaoSession().getAdviceBeanDao().insert(adviceBean);
        etContent.setText("");
        etTitle.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
