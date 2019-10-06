package com.lanshiqin.wechatluckymoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;

import com.lanshiqin.wechatluckymoney.constant.ConfigConsts;
import com.lanshiqin.wechatluckymoney.core.BaseActivity;

public class MainActivity extends BaseActivity {

    private SwitchCompat luckyMoneySwitch;
    private AppCompatEditText luckyMoneyEditText;
    private AppCompatButton saveButton;
    private Boolean luckyMoneyStatus = true;
    private String luckyMoneyValue = null;
    private SharedPreferences sharedPreferences;


    @Override
    protected void initView(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_main);
        luckyMoneySwitch = findViewById(R.id.luckyMoneySwitch);
        luckyMoneyEditText = findViewById(R.id.luckyMoneyEditText);
        saveButton = findViewById(R.id.saveButton);
    }

    @Override
    protected void initDate() {
        sharedPreferences = getSharedPreferences(ConfigConsts.APP_SHARD_PREFERENCES_FILE_NAME, Context.MODE_WORLD_READABLE);
        luckyMoneyStatus = sharedPreferences.getBoolean("luckyMoneyStatus",true);
        luckyMoneyValue = sharedPreferences.getString("luckyMoneyValue",ConfigConsts.WECHAT_LUCKY_MONEY_VALUE);
        luckyMoneySwitch.setChecked(luckyMoneyStatus);
        luckyMoneyEditText.setText(luckyMoneyValue);
        if (luckyMoneyStatus){
            luckyMoneyEditText.setVisibility(View.VISIBLE);
        }else{
            luckyMoneyEditText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initAction() {
        luckyMoneySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                luckyMoneyStatus = isChecked;
                if (luckyMoneyStatus){
                    luckyMoneyEditText.setVisibility(View.VISIBLE);
                }else{
                    luckyMoneyEditText.setVisibility(View.GONE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (!luckyMoneyStatus){
                    editor.putBoolean("luckyMoneyStatus", false);
                    editor.apply();
                    return;
                }
                if (luckyMoneyEditText.getText()==null || luckyMoneyEditText.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "自定义红包金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                luckyMoneyValue = luckyMoneyEditText.getText().toString();
                editor.putBoolean("luckyMoneyStatus",luckyMoneyStatus);
                editor.putString("luckyMoneyValue",luckyMoneyValue);
                editor.apply();
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
