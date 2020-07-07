package com.dharyiswara.tiket.test.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
        initEvent()
        loadingData()
        observeData()
    }

    override fun initView() {}

    override fun initEvent() {}

    override fun loadingData(isFromSwipe: Boolean) {}

    override fun observeData() {}

}