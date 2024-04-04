package com.korashiGroup.salamtakPatient.fragment.webView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.korashiGroup.salamtakPatient.databinding.PrivacyFragmentBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class PrivacyPolicyFragment : Fragment() {

    //    private lateinit var webView: WebView
    private lateinit var binding: PrivacyFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PrivacyFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initview()
    }

    private fun initview() {
        if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "en") {
            createLink("https://salamtakgroup.com/privacyPolicy.html")
        } else {
            createLink("https://salamtakgroup.com/PrivacyPolicyAr.html")
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