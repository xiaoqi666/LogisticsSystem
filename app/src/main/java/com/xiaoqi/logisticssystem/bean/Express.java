package com.xiaoqi.logisticssystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 快递类
 */
@Entity
public class Express implements Parcelable /*尽心序列化*/ {
    @Id(autoincrement = true)
    private Long id;
    private String userId;//用户ID,外键
    private String name;//寄送物品名称
    private String type;//寄送类型

    private String chName;//寄送人
    private String sendAddress;//发件地址
    private String sendPhone;//发件人电话

    private String receAddress;//收件地址
    private String receName;//收件人
    private String recePhone;//收件人电话

    private String trackNumber;//快递单号
    private String payType;//支付方式

    private int status;//状态,0 下单完成,1为递送中,2已送到


    protected Express(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userId = in.readString();
        name = in.readString();
        type = in.readString();
        chName = in.readString();
        sendAddress = in.readString();
        sendPhone = in.readString();
        receAddress = in.readString();
        receName = in.readString();
        recePhone = in.readString();
        trackNumber = in.readString();
        payType = in.readString();
        status = in.readInt();
    }

    @Generated(hash = 170022614)
    public Express(Long id, String userId, String name, String type, String chName,
            String sendAddress, String sendPhone, String receAddress,
            String receName, String recePhone, String trackNumber, String payType,
            int status) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.chName = chName;
        this.sendAddress = sendAddress;
        this.sendPhone = sendPhone;
        this.receAddress = receAddress;
        this.receName = receName;
        this.recePhone = recePhone;
        this.trackNumber = trackNumber;
        this.payType = payType;
        this.status = status;
    }

    @Generated(hash = 760607181)
    public Express() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(chName);
        dest.writeString(sendAddress);
        dest.writeString(sendPhone);
        dest.writeString(receAddress);
        dest.writeString(receName);
        dest.writeString(recePhone);
        dest.writeString(trackNumber);
        dest.writeString(payType);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChName() {
        return this.chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getSendAddress() {
        return this.sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendPhone() {
        return this.sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getReceAddress() {
        return this.receAddress;
    }

    public void setReceAddress(String receAddress) {
        this.receAddress = receAddress;
    }

    public String getReceName() {
        return this.receName;
    }

    public void setReceName(String receName) {
        this.receName = receName;
    }

    public String getRecePhone() {
        return this.recePhone;
    }

    public void setRecePhone(String recePhone) {
        this.recePhone = recePhone;
    }

    public String getTrackNumber() {
        return this.trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final Creator<Express> CREATOR = new Creator<Express>() {
        @Override
        public Express createFromParcel(Parcel in) {
            return new Express(in);
        }

        @Override
        public Express[] newArray(int size) {
            return new Express[size];
        }
    };
}
