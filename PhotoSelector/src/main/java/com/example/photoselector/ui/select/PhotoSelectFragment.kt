package com.example.photoselector.ui.select

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoselector.R
import com.example.photoselector.data.local.PhotoRequest
import com.example.photoselector.repository.MediaRepositoryImpl
import com.example.photoselector.ui.EXTRA_PHOTO_PICK_REQUEST
import com.example.photoselector.utils.px
import com.example.photoselector.widget.GridSpaceItemDecoration
import kotlinx.coroutines.launch

class PhotoSelectFragment : Fragment() {

    private lateinit var progressLoading: View
    private lateinit var rvMediaList: RecyclerView
    private lateinit var viewModel: PhotoSelectViewModel
    private lateinit var request: PhotoRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = MediaRepositoryImpl(requireContext())
        request = arguments?.getParcelable(EXTRA_PHOTO_PICK_REQUEST)
            ?: throw IllegalArgumentException("没有找到参数PhotoRequest")
        viewModel = ViewModelProvider(
            this,
            PhotoSelectViewModel.FACTORY(repo)
        )[PhotoSelectViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isLoading) {
                        progressLoading.visibility = View.VISIBLE
                        rvMediaList.visibility = View.GONE
                        return@collect
                    }
                    progressLoading.visibility = View.GONE
                    rvMediaList.also {
                        it.visibility = View.VISIBLE
                        it.adapter =
                            MediaAdapter(uiState.mediaItems.toMutableList(), request, mCallback)
                        if (it.itemDecorationCount <= 0) {
                            it.addItemDecoration(GridSpaceItemDecoration(3, 3.px, 3.px))
                        }
                    }
                }
            }
        }
    }

    private var mCallback: ((List<Uri>) -> Unit)? = null

    fun setOnItemSelected(callback: (List<Uri>) -> Unit) {
        mCallback = callback
    }

    public

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_picture_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressLoading = view.findViewById(R.id.progress_loading)
        rvMediaList = view.findViewById<RecyclerView?>(R.id.rv_media_list).apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

}