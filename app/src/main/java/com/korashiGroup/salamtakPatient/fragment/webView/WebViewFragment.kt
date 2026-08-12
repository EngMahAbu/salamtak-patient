package com.korashiGroup.salamtakPatient.fragment.webView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.korashiGroup.salamtakPatient.databinding.FragmentWebViewBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class WebViewFragment : Fragment() {

    //    private lateinit var webView: WebView
    private lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initview()
    }

    private fun initview() {
        if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "en") {
            createLink("https://salamtakgroup.com/en/termsOf")
        } else {
            createLink("https://salamtakgroup.com/ar/شروط-الاستخدام")
        }
    }

    private fun createLink(link: String) {
        binding.webview.apply {
            webViewClient = WebViewClient()
            loadUrl(link)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }

    }

}