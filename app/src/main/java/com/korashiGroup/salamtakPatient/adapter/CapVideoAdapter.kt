package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutCapsBinding
import com.korashiGroup.salamtakPatient.model.salamtakgate.blogs.DataBlogs

class CapVideoAdapter: ListAdapter<DataBlogs, CapVideoAdapter.ViewHolder>(CapVideoAdapter.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLayoutCapsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
//
//            val videoid=if (model.image!!.contains("v=") ) {
//                model.image.split("=").toTypedArray()[1]
//            } else{
//                if (model.image.contains("youtu.be/")){
//                    model.image.substringAfter("youtu.be/")+model.image.substringBefore("?")
//                }else{
//                    model.image.substringAfter("shorts/")+model.image.substringBefore("?")
//                }
//            }

        val videoId = when {
            model.image!!.contains("v=") ->
                model.image.substringAfter("v=").substringBefore("&")
            model.image.contains("youtu.be/") ->
                model.image.substringAfter("youtu.be/").substringBefore("?")
            model.image.contains("shorts/") ->
                model.image.substringAfter("shorts/").substringBefore("?")
            else -> ""
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


            holder.binding.youtubePlayerView.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webChromeClient = object : WebChromeClient() {
                    private var customView: View? = null
                    private var customViewCallback: CustomViewCallback? = null

                    private fun getActivity(context: android.content.Context?): android.app.Activity? {
                        if (context == null) return null
                        if (context is android.app.Activity) return context
                        if (context is android.content.ContextWrapper) return getActivity(context.baseContext)
                        return null
                    }

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        if (customView != null) {
                            callback?.onCustomViewHidden()
                            return
                        }
                        customView = view
                        customViewCallback = callback
                        
                        val activity = getActivity(holder.itemView.context)
                        val decorView = activity?.window?.decorView as? android.widget.FrameLayout
                        
                        // Hide system UI for true full screen
                        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                                
                        decorView?.addView(customView, android.widget.FrameLayout.LayoutParams(
                            android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                            android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                        ))
                    }

                    override fun onHideCustomView() {
                        val activity = getActivity(holder.itemView.context)
                        val decorView = activity?.window?.decorView as? android.widget.FrameLayout
                        decorView?.removeView(customView)
                        customView = null
                        
                        // Restore system UI
                        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                        
                        customViewCallback?.onCustomViewHidden()
                    }
                }

                webViewClient = object : android.webkit.WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                        val url = request?.url?.toString()
                        if (url != null && (url.contains("youtube.com") || url.contains("youtu.be"))) {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                            holder.itemView.context.startActivity(intent)
                            return true
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, url: String?): Boolean {
                        if (url != null && (url.contains("youtube.com") || url.contains("youtu.be"))) {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                            holder.itemView.context.startActivity(intent)
                            return true
                        }
                        return super.shouldOverrideUrlLoading(view, url)
                    }
                }
                
                val iframeHtml = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${videoId}?enablejsapi=1&origin=https://salamtak.com\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                
                loadDataWithBaseURL("https://salamtak.com", iframeHtml, "text/html", "utf-8", null)
            }



    }

    class ViewHolder(itemBinding: ItemLayoutCapsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutCapsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataBlogs>() {
        override fun areItemsTheSame(oldItem: DataBlogs, newItem: DataBlogs): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataBlogs, newItem: DataBlogs): Boolean {
            return true
        }
    }



}