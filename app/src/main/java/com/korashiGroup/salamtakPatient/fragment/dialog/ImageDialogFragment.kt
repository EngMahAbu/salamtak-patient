package com.korashiGroup.salamtakPatient.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.DialogImageViewerBinding

class ImageDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_IMAGE_RES = "image_res"

        fun newInstance(imageRes: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            val args = Bundle()
            args.putString(ARG_IMAGE_RES, imageRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  binding=DialogImageViewerBinding.inflate(layoutInflater,container,false)
        val view = inflater.inflate(R.layout.dialog_image_viewer, container, false)

        val photoView = view.findViewById<ShapeableImageView>(R.id.image)
        val imageRes = arguments?.getString(ARG_IMAGE_RES) ?: 0

        if (imageRes != 0) {
            Glide.with(photoView)
                .load("https://salamtechapi.azurewebsites.net/${imageRes}")
                .into(photoView)
        }

        // إغلاق الـ Dialog عند الضغط على الخلفية
        view.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }
}