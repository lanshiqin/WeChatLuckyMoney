package com.lanshiqin.wechatluckymoney.core;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void initDate();
    protected abstract void initAction();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        initDate();
        initAction();
    }
}