package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSliderBinding
import com.korashiGroup.salamtakPatient.model.HomeAdsData


class ImageVideoAdapter : ListAdapter<HomeAdsData, ImageVideoAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLayoutSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)


        val videoid=if (model.VideoLink.contains("v=") ) {
            model.VideoLink.split("=").toTypedArray()[1].substringBefore("&")
        } else{
            if (model.VideoLink.contains("youtu.be/")){
                model.VideoLink.substringAfter("youtu.be/")+model.VideoLink.substringBefore("?")
            }else{
                model.VideoLink.substringAfter("shorts/")+model.VideoLink.substringBefore("?")
            }
        }
        holder.binding.youtubePlayerView.visibility = View.VISIBLE
        //           holder.binding.ivHome.visibility = View.GONE
//                holder.binding.youtubePlayerView.apply {
//                    this.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                        override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
//                            val videoId =if(videoid.size==3){
//                                "${videoid[1]}=${videoid[2]}"
//                            }else{
//                                "${videoid[1]}"
//                            }
//                            youTubePlayer.cueVideo(videoId, 0f)
//                            youTubePlayer.loadVideo(videoId, 0f)
//                            youTubePlayer.pause()
//                        }
//
//
//                    })
//                }

//
        holder.binding.youtubePlayerView.apply {
            //  val videoId =videoid[1].substringBefore("&")
            this.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${videoid}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
                "text/html","utf-8")
            settings.javaScriptEnabled=true
            webChromeClient= WebChromeClient()
        }


    }

    class ViewHolder(itemBinding: ItemLayoutSliderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSliderBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<HomeAdsData>() {
        override fun areItemsTheSame(oldItem: HomeAdsData, newItem: HomeAdsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeAdsData, newItem: HomeAdsData): Boolean {
            return true
        }
    }


}