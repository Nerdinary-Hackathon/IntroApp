package com.example.introapp.presentation.ui.received_card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.introapp.R
import com.example.introapp.databinding.ItemReceivedCardBinding
import com.example.introapp.domain.entity.CardSummary
import com.example.introapp.domain.entity.JobGroup

class ReceivedCardAdapter : ListAdapter<CardSummary, ReceivedCardAdapter.ReceivedCardViewHolder>(CardSummaryDiffCallback()) {

    private var onItemClickListener: ((CardSummary) -> Unit)? = null

    fun setOnItemClickListener(listener: (CardSummary) -> Unit) {
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

        fun bind(item: CardSummary, onItemClick: ((CardSummary) -> Unit)?) {
            binding.apply {
                // 프로필 이미지
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
                tvReceivedCardNickname.text = item.nickname

                // 직군 (enum의 displayName 사용)
                tvReceivedCardJob.text = item.jobGroup.displayName

                // 직군에 따라 텍스트 색상 변경
                val textColor = getJobGroupColor(item.jobGroup)
                tvReceivedCardNickname.setTextColor(textColor)
                tvReceivedCardJob.setTextColor(textColor)

                root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }

        /**
         * 직군에 따른 색상을 반환하는 함수
         */
        private fun getJobGroupColor(jobGroup: JobGroup): Int {
            val context = binding.root.context

            // JobGroup enum에 따라 적절한 색상 리소스 반환
            return when (jobGroup) {
                JobGroup.PM -> ContextCompat.getColor(context, R.color.pm_deep)
                JobGroup.DESIGNER -> ContextCompat.getColor(context, R.color.design_deep)
                JobGroup.WEB -> ContextCompat.getColor(context, R.color.web_deep)
                JobGroup.BACKEND -> ContextCompat.getColor(context, R.color.backend_deep)
                JobGroup.ANDROID -> ContextCompat.getColor(context, R.color.android_deep)
                JobGroup.IOS -> ContextCompat.getColor(context, R.color.ios_deep)
            }
        }
    }

    private class CardSummaryDiffCallback : DiffUtil.ItemCallback<CardSummary>() {
        override fun areItemsTheSame(oldItem: CardSummary, newItem: CardSummary): Boolean {
            // nickname으로 아이템 식별 (userId가 없으므로)
            return oldItem.nickname == newItem.nickname && oldItem.jobGroup == newItem.jobGroup
        }

        override fun areContentsTheSame(oldItem: CardSummary, newItem: CardSummary): Boolean {
            return oldItem == newItem
        }
    }
}