package com.xiaoqi.logisticssystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AdviceBean implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;
    private String dateStr;
    private String userId;

    

    protected AdviceBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
        content = in.readString();
        dateStr = in.readString();
        userId = in.readString();
    }

    @Generated(hash = 1884083665)
    public AdviceBean(Long id, String title, String content, String dateStr,
            String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateStr = dateStr;
        this.userId = userId;
    }

    @Generated(hash = 300252844)
    public AdviceBean() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(dateStr);
        dest.writeString(userId);
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static final Creator<AdviceBean> CREATOR = new Creator<AdviceBean>() {
        @Override
        public AdviceBean createFromParcel(Parcel in) {
            return new AdviceBean(in);
        }

        @Override
        public AdviceBean[] newArray(int size) {
            return new AdviceBean[size];
        }
    };
}
