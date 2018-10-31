package com.xiaoqi.logisticssystem.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

/*

@Entity 用于标识这是一个需要Greendao帮我们生成代码的bean

@Id 标明主键，括号里可以指定是否自增

@Property 用于设置属性在数据库中的列名（默认不写就是保持一致）

@NotNull 非空

@Transient 标识这个字段是自定义的不会创建到数据库表里

@Unique 添加唯一约束


@ToOne 是将自己的一个属性与另一个表建立关联（外键）
@ToMany的属性referencedJoinProperty，类似于外键约束。

@JoinProperty 对于更复杂的关系，可以使用这个注解标明目标属性的源属性。

 */
@Entity
public class Customer {
    @Id(autoincrement = true)
    private Long id;
    private String userId;//用户ID,外键
    private String username;
    private String phone;
    private String chName;
    private String password;
    private int level;//1级为管理员，2级为普通用户
    private String address;
    @Generated(hash = 2118300702)
    public Customer(Long id, String userId, String username, String phone,
            String chName, String password, int level, String address) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.chName = chName;
        this.password = password;
        this.level = level;
        this.address = address;
    }
    @Generated(hash = 60841032)
    public Customer() {
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
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getChName() {
        return this.chName;
    }
    public void setChName(String chName) {
        this.chName = chName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }



}
