package com.example.introapp.presentation.ui.received_card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.introapp.R
import com.example.introapp.data.model.response.CardItem
import com.example.introapp.databinding.ItemReceivedCardBinding

class ReceivedCardAdapter : ListAdapter<CardItem, ReceivedCardAdapter.ReceivedCardViewHolder>(CardItemDiffCallback()) {

    private var onItemClickListener: ((CardItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CardItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedCardViewHolder {
        val binding = ItemReceivedCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReceivedCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReceivedCardViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class ReceivedCardViewHolder(
        private val binding: ItemReceivedCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardItem, onItemClick: ((CardItem) -> Unit)?) {
            binding.apply {
                // 프로필 이미지
//                ivReceivedCardProfile.load(item.profileImg) {
//                    crossfade(true)
//                    placeholder(R.drawable.android_onboarding_profile)
//                    error(R.drawable.android_onboarding_profile)
//                }
                if (item.profileImg.startsWith("http")) {
                    // URL인 경우
                    ivReceivedCardProfile.load(item.profileImg) {
                        crossfade(true)
                        placeholder(R.drawable.android_onboarding_profile)
                        error(R.drawable.android_onboarding_profile)
                    }
                } else {
                    // 드로어블 리소스 이름인 경우
                    val resourceId = binding.root.context.resources.getIdentifier(
                        item.profileImg,
                        "drawable",
                        binding.root.context.packageName
                    )
                    ivReceivedCardProfile.load(resourceId) {
                        crossfade(true)
                        error(R.drawable.android_onboarding_profile)
                    }
                }

                // 닉네임
                tvReceivedCardNickname.text = item.nickName

                // 직군
                tvReceivedCardJob.text = item.jobGroup

                root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }
    }

    // DiffUtil.ItemCallback 구현
    private class CardItemDiffCallback : DiffUtil.ItemCallback<CardItem>() {
        override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
            return oldItem.nickName == newItem.nickName && oldItem.jobGroup == newItem.jobGroup
        }

        override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
            return oldItem == newItem
        }
    }
}