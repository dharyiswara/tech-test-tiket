package com.dharyiswara.tiket.test.ui

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dharyiswara.tiket.test.R
import com.dharyiswara.tiket.test.base.BaseActivity
import com.dharyiswara.tiket.test.helper.CommonConstants.LIMIT
import com.dharyiswara.tiket.test.helper.Status
import com.dharyiswara.tiket.test.helper.TextUtils
import com.dharyiswara.tiket.test.helper.extension.gone
import com.dharyiswara.tiket.test.helper.extension.hideKeyboard
import com.dharyiswara.tiket.test.helper.extension.visible
import com.dharyiswara.tiket.test.model.UserResult
import com.dharyiswara.tiket.test.ui.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val userAdapter by lazy { UserAdapter() }

    private var page = 1
    private var totalPage = 999
    private var query = TextUtils.BLANK
    private var isLoading = false

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initView() {
        super.initView()

        with(rvUser) {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun initEvent() {
        super.initEvent()

        btnSearch.setOnClickListener {
            startSearchUser()
        }

        nsListUser.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (v.getChildAt(v.childCount - 1) != null && page <= totalPage && !isLoading) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY
                    ) {
                        searchUser(query, page)
                    }
                }
            })

        etSearchUser.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && etSearchUser.text.toString().isNotEmpty()) {
                v.hideKeyboard()
                v.clearFocus()
                startSearchUser()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun startSearchUser() {
        query = etSearchUser.text.toString()
        page = 1
        tvEmptyUser.gone()
        userAdapter.clearData()
        searchUser(query, page)
    }

    private fun searchUser(query: String, page: Int) {
        viewModel.getUser(query, page)
    }

    override fun observeData() {
        super.observeData()

        viewModel.userListLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> startLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    it.data?.let { result ->
                        getUserSuccess(result)
                    }
                }
                Status.LIMIT -> toast("Limit")
                Status.EMPTY -> {
                    hideLoading()
                    showUserNotFound()
                }
                Status.ERROR -> {
                    hideLoading()
                    it.throwable?.printStackTrace()
                }
            }
        })
    }

    private fun startLoading() {
        if (userAdapter.itemCount == 0) {
            progressBar.visible()
            rvUser.gone()
        }
    }

    private fun hideLoading() {
        if (userAdapter.itemCount == 0) {
            progressBar.gone()
            rvUser.visible()
        }
    }

    private fun getUserSuccess(result: UserResult) {
        if (page == 1) {
            totalPage = (result.totalCount / LIMIT).toDouble().roundToInt()
        }

        if (result.userList.isNotEmpty()) {
            userAdapter.addData(result.userList)
        } else {
            showUserNotFound()
        }
        page++
    }

    private fun showUserNotFound() {
        rvUser.gone()
        tvEmptyUser.visible()
    }

}
