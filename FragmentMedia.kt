package com.rajapps.wastatussaver.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rajapps.wastatussaver.data.StatusRepo
import com.rajapps.wastatussaver.databinding.FragmentMediaBinding
import com.rajapps.wastatussaver.models.MediaModel
import com.rajapps.wastatussaver.utils.Constants
import com.rajapps.wastatussaver.viewmodels.StatusViewModel
import com.rajapps.wastatussaver.viewmodels.factories.StatusViewModelFactory
import com.rajapps.wastatussaver.views.adapters.MediaAdapter


class FragmentMedia : Fragment() {
    private val binding by lazy {
        FragmentMediaBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: StatusViewModel
    lateinit var adapter: MediaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            arguments?.let {

                val repo = StatusRepo(requireActivity())
                viewModel= ViewModelProvider(requireActivity(), StatusViewModelFactory(repo))[StatusViewModel::class.java]

                val mediaType = it.getString(Constants.MEDIA_TYPE_KEY,"")
               // tempMedia.text = mediaType

                when(mediaType){
                    Constants.MEDIA_TYPE_WHATSAPP_IMAGES->{
                        viewModel.whatsAppImagesLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.filename
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_VIDEOS->{
                        viewModel.whatsAppVideosLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.filename
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES->{
                        viewModel.whatsAppBusinessImagesLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.filename
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS->{
                        viewModel.whatsAppBusinessVideosLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.filename
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                        }
                    }




                }

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}
