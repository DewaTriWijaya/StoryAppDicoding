package com.andflube.storyapp.ui.list

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import com.andflube.storyapp.R
import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.ui.detail.DetailsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AdapterListStory :
    PagingDataAdapter<StoryDB, AdapterListStory.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var ivItemPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        private var tvItemCreated: TextView = itemView.findViewById(R.id.tv_item_created)

        fun bind(story: StoryDB) {
            tvItemName.text = story.name
            tvItemCreated.text = story.createdAt

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(ivItemPhoto)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRA_NAME, story.name)
                intent.putExtra(
                    DetailsActivity.EXTRA_CREATED,
                    DateFormat.formatDate(story.createdAt)
                )
                intent.putExtra(DetailsActivity.EXTRA_DESC, story.description)
                intent.putExtra(DetailsActivity.EXTRA_IMAGE, story.photoUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivItemPhoto, "photo"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemCreated, "created"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryDB>() {
            override fun areItemsTheSame(oldItem: StoryDB, newItem: StoryDB): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryDB, newItem: StoryDB): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}