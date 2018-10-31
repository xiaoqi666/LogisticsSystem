package com.xiaoqi.logisticssystem.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Express;
import com.xiaoqi.logisticssystem.db.ExpressDBUtils;
import com.xiaoqi.logisticssystem.utils.CompressImage;
import com.xiaoqi.logisticssystem.utils.RegexUtils;
import com.xiaoqi.logisticssystem.utils.ZxingQrCodeUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 寄件页面
 */
public class SendFragment extends Fragment {

    private static final String[] types = new String[]{"圆通", "申通", "韵达", "中通", "EMS", "天天", "汇通", "邮政"};
    private static final String[] payType = new String[]{"在线支付", "到付", "面对面"};
    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.et_goodname)
    EditText etGoodname;
    @BindView(R.id.et_sendname)
    EditText etSendname;
    @BindView(R.id.et_sendaddr)
    EditText etSendaddr;
    @BindView(R.id.et_senphone)
    EditText etSenphone;
    @BindView(R.id.et_recename)
    EditText etRecename;
    @BindView(R.id.et_recephone)
    EditText etRecephone;
    @BindView(R.id.et_receaddr)
    EditText etReceaddr;
    @BindView(R.id.tv_ticknumber)
    TextView tvTicknumber;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;
    @BindView(R.id.sp_pay)
    Spinner spPay;
    private AlertDialog alertDialog;
    private Date date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_send, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        spType.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_tv_sp, types));
        spPay.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_tv_sp, payType));
        date = new Date();
        tvTicknumber.setText("快递单号:" + date.getTime());
    }


    @OnClick(R.id.btn_commit)
    void onClick() {
        if (date == null)
            date = new Date();
        tvTicknumber.setText("快递单号:" + date.getTime());
        String goodName = etGoodname.getText().toString();
        String receAddr = etReceaddr.getText().toString();
        String recename = etRecename.getText().toString();
        String recephone = etRecephone.getText().toString();
        String sendname = etSendname.getText().toString();
        String sendAddre = etSendaddr.getText().toString();
        String senPhone = etSenphone.getText().toString();


        int selectedItemPosition = spType.getSelectedItemPosition();//获取选中的条目
        String type = types[selectedItemPosition];

        if (TextUtils.isEmpty(goodName) ||
                TextUtils.isEmpty(receAddr) ||
                TextUtils.isEmpty(recename) ||
                TextUtils.isEmpty(recephone) ||
                TextUtils.isEmpty(sendname) ||
                TextUtils.isEmpty(sendAddre) ||
                TextUtils.isEmpty(senPhone)) {
            AppClient.showToast("寄件信息不能为空");
            return;
        }

        if (!RegexUtils.checkMobile(recephone) || !RegexUtils.checkMobile(senPhone)) {
            AppClient.showToast("手机号码不符合规范");
            return;
        }

        int position = spPay.getSelectedItemPosition();

        final Express express = new Express();
        express.setName(goodName);
        express.setType(type);

        express.setChName(sendname);
        express.setSendAddress(sendAddre);
        express.setSendPhone(senPhone);

        express.setReceAddress(receAddr);
        express.setRecePhone(recephone);
        express.setReceName(recename);
        //生成订单号
        express.setTrackNumber(date.getTime() + "");

        express.setPayType(payType[position]);
        switch (position) {
            case 0://在线支付(二维码支付)
                showPayDialog(express);
                break;
            case 1://到付
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("您选择了到付,将由收件人支付邮费");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        payFinash(express);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

                break;
            case 2://面对面
                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("您选择了面对面,将由快递员取件时收取")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                payFinash(express);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
        date = null;
    }


    private void payFinash(Express express) {
        etGoodname.setText("");
        tvTicknumber.setText("");
        etReceaddr.setText("");
        etRecename.setText("");
        etRecephone.setText("");
        etSendname.setText("");
        etSendaddr.setText("");
        etSenphone.setText("");
        ExpressDBUtils.insert(express);
        AppClient.showToast("订单提交成功,详细信息可在寄件信息中查看");
    }


    /**
     * 显示支付的对话框
     */
    private void showPayDialog(final Express bed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.item_dialog_pay, null);
        builder.setView(view);
        ImageView ivZxing = (ImageView) view.findViewById(R.id.iv_zxing);
        Button btnFinash = (Button) view.findViewById(R.id.btn_finash);
        Button btnQuxiao = (Button) view.findViewById(R.id.btn_quxiao);
        TextView tv_name = view.findViewById(R.id.tv_pay);
        tv_name.setText("扫描下方二维码支付:" + 10 + "元");

        String content = bed.getName() + "成功支付10元\n" +
                "(假装支付成功了)";
        Bitmap qrCodeWithLogo = ZxingQrCodeUtil.createQRCodeWithLogo(content, 300, CompressImage.readBitMap(getActivity(), R.drawable.touxiang));
        ivZxing.setImageBitmap(qrCodeWithLogo);

        btnQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnFinash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payFinash(bed);
                alertDialog.dismiss();

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
