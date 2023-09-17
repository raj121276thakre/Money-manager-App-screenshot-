package com.rajapps.wastatussaver.views.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.OnActivityResultListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rajapps.wastatussaver.data.StatusRepo
import com.rajapps.wastatussaver.utils.Constants
import com.rajapps.wastatussaver.databinding.FragmentStatusBinding
import com.rajapps.wastatussaver.utils.SharedPrefKeys
import com.rajapps.wastatussaver.utils.SharedPrefUtils
import com.rajapps.wastatussaver.utils.getFolderPermissions
import com.rajapps.wastatussaver.viewmodels.StatusViewModel
import com.rajapps.wastatussaver.viewmodels.factories.StatusViewModelFactory
import com.rajapps.wastatussaver.views.adapters.MediaViewPagerAdapter

class FragmentStatus : Fragment() {
    private val binding by lazy {
        FragmentStatusBinding.inflate(layoutInflater)
    }

    private lateinit var type:String
    private val WHATSAPP_REQUEST_CODE = 101
    private val WHATSAPP_Business_REQUEST_CODE = 102

    private val viewPagerTitles = arrayListOf("Images","Videos")
    lateinit var viewModel:StatusViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {


            arguments?.let {

                val repo = StatusRepo(requireActivity())
                viewModel= ViewModelProvider(requireActivity(),StatusViewModelFactory(repo))[StatusViewModel::class.java]

              type = it.getString(Constants.FRAGMENT_TYPE_KEY,"")
              tempText.text = type
                when(type){
                    Constants.TYPE_WHATSAPP_MAIN->{
                        //whatsapp
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,false)
                        if (isPermissionGranted){
                            getWhatsAppStatuses()
                        }

                        permissionLayout.btnPermission.setOnClickListener{
                            getFolderPermissions(
                                context = requireContext(),
                                REQUEST_CODE = WHATSAPP_REQUEST_CODE,
                                initialUri = Constants.getWhatsappUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity())

                        statusViewPager.adapter = viewPagerAdapter
                       TabLayoutMediator(tabLayout, statusViewPager){tab,pos->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                    }

                    Constants.TYPE_WHATSAPP_BUSINESS->{
                        //whatsapp Business
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,false)
                        if (isPermissionGranted){
                            getWhatsAppBusinessStatuses()
                        }

                        permissionLayout.btnPermission.setOnClickListener{
                            getFolderPermissions(
                                context = requireContext(),
                                REQUEST_CODE = WHATSAPP_Business_REQUEST_CODE,
                                initialUri = Constants.getWhatsappBusinessUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES,
                            videosType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS
                            )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager){tab,pos->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                    }

                }

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    fun getWhatsAppStatuses(){
        // function to get Whatsapp statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppStatuses()
    }

    fun getWhatsAppBusinessStatuses(){
        // function to get Whatsapp Business statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppBusinessStatuses()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==AppCompatActivity.RESULT_OK){
            val treeUri =data?.data!!

            requireActivity().contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (requestCode == WHATSAPP_REQUEST_CODE){
                //whatsapp logic
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,true)
                getWhatsAppStatuses()
            }

            else  if (requestCode == WHATSAPP_Business_REQUEST_CODE){
                //whatsapp Business logic
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,true)
                getWhatsAppBusinessStatuses()

            }

        }

    }


}
