package com.phillipkaundeni265.ElderJoy1

import android.Manifest
import android.content.Context
import android.net.Uri
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var uploadButton: Button

    companion object {
        const val PROFILE_PICTURE_URI_KEY = "profile_picture_uri_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.profile_image)
        uploadButton = view.findViewById(R.id.upload_profile_picture)

        // Load saved profile picture URI if it exists
        loadSavedProfilePicture()

        uploadButton.setOnClickListener {
            checkAndRequestPermissions()
        }

        return view
    }

    private fun loadSavedProfilePicture() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedImageUriString = sharedPreferences.getString(PROFILE_PICTURE_URI_KEY, null)

        if (savedImageUriString != null) {
            val savedImageUri = Uri.parse(savedImageUriString)
            // Check if the URI is valid before setting the image
            if (savedImageUri != null) {
                try {
                    profileImageView.setImageURI(savedImageUri)
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveProfilePicture(uri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(PROFILE_PICTURE_URI_KEY, uri.toString())
            apply()
        }
    }

    // Activity result launcher for picking an image from the gallery
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            profileImageView.setImageURI(uri)
            saveProfilePicture(uri)
        } else {
            Toast.makeText(context, "Image selection failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Activity result launcher for requesting storage permission
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // If Android version is 10 or above, you don't need READ_EXTERNAL_STORAGE permission for media access.
            openGallery()
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is already granted, proceed with uploading the image
                openGallery()
            } else {
                // Request permission using the new ActivityResultLauncher
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        // Use ActivityResultLauncher to open the gallery and pick an image
        pickImageLauncher.launch("image/*")
    }
}
