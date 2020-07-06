package com.dharyiswara.tiket.test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dharyiswara.tiket.test.R
import com.dharyiswara.tiket.test.helper.extension.loadFromUrl
import com.dharyiswara.tiket.test.model.User
import kotlinx.android.synthetic.main.layout_item_user_list.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_user_list, parent, false)
        )

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun addData(list: List<User>) {
        userList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        userList.clear()
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(user: User) {
            with(itemView) {
                ivUser.loadFromUrl(user.avatarUrl)
                tvUserName.text = user.login
            }
        }

    }

}