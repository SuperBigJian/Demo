package com.cyaan.demo.binder;

import com.cyaan.demo.binder.remote.UserInfo;

interface ITestAidlInterface {

    void getInfoById(int id,out UserInfo user );

    UserInfo getUserInfo(in UserInfo user);

    UserInfo getUserInfo2(inout UserInfo user);
}