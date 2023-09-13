package com.korashiGroup.salamtakPatient.fragment.webView
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.korashiGroup.salamtakPatient.databinding.FragmentWebViewBinding

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

        binding.webview.apply {

            webViewClient = WebViewClient()
            loadUrl("https://doctor.salamtakgroup.com/terms")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

        }
    }
}