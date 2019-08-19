package com.naver.ers;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.naver.ers.CustomData;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CustomDataTest {

    @Test
    public void putString() {
        CustomData customData = new CustomData.Builder()
                .putData("Test1", "Value1")
                .build();
        System.out.println(customData.getData());
    }

    @Test
    public void putInt() {
        CustomData customData = new CustomData.Builder()
                .putData("Test1", 100)
                .build();
        System.out.println(customData.getData());
    }

    @Test
    public void putFloat() {
        CustomData customData = new CustomData.Builder()
                .putData("Test1", 100.1f)
                .build();
        System.out.println(customData.getData());
    }

    @Test
    public void putBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("Test1", "Value1");
        bundle.putInt("Test2", 100);
        bundle.putFloat("Test3", 10.4f);
        CustomData customData = new CustomData.Builder()
                .putData(bundle)
                .build();
        System.out.println(customData.getData());
    }

    @Test
    public void putAll() {
        CustomData customData = new CustomData.Builder()
                .putData("String", "Value1")
                .putData("Int", 100)
                .putData("Float", 100.1f)
                .build();
        System.out.println(customData.getData());
    }

}