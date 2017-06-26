package com.feng.retrofit.model;

import java.io.Serializable;

/**
 * Created by Chexiangjia-MAC on 17/6/26.
 */

public class UserModel implements Serializable {
    public UserModel(){
        this.name = "";
        this.sex = "";
        this.birthday = "";
        this.serviceYears = "";
        this.jobTitle = "";
        this.departmentName = "";
        this.signature = "";
        this.wellKnownSignature = "";

        this.countFollowers = 0;
        this.countUsersBeFollowed = 0;

        this.countCommentNotRead = 0;
        this.countAwardNotRead = 0;
        this.countAppreciateNotRead = 0;
        this.countContentAtNotRead = 0;
        this.createdTimestamp = 0;
    }

    public String name;
    public String nickName;
    public String token;
    public String sex;
    public int age;
    public String mobilePhone;
    public String icon;
    public MetaLevelModel level;
    public MetaVFlagModel vflag;
    public String bgPicture;
    public String signature;
    public String wellKnownSignature;
    public String birthday;
    public String departmentName;
    public String jobTitle;
    public String serviceYears;
    public boolean showWorkbench;
    public MetaCxjStaffModel imFlag;
    public boolean signed;
    public long countFollowers;
    public long countUsersBeFollowed;
    public boolean followed;
    public long countMainPageBeHitted;
    public long totalContents;
    public long countUserPoints;

    public long countCommentNotRead;
    public long countAwardNotRead;
    public long countAppreciateNotRead;
    public long countContentAtNotRead;

    public long createdTimestamp;

    private class MetaCxjStaffModel {
        public boolean active;
        public String empNo;
        public long storeId;
        public String storeNo;
        public String userSign;
        public String appId;
        public String cityName;
        public String cityCode;
        public String position;
        public String profilePicUrl;
        public String nickName;
    }

    private class MetaLevelModel implements Serializable{

        public String name;
        public String icon;
        public int value;
    }

    private class MetaVFlagModel implements Serializable{
        public String name;
        public String icon;
    }
}
