package com.thoughtctl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtctl.adapter.SearchResultsAdapter
import com.thoughtctl.api.UiState
import com.thoughtctl.databinding.ActivitySearchBinding
import com.thoughtctl.model.ImgurModel
import com.thoughtctl.model.ImgurResponseModel
import com.thoughtctl.model.SearchRequestModel
import com.thoughtctl.utils.GridSpacingItemDecoration
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private lateinit var searchViewModel: SearchViewModel

    private lateinit var mBinding: ActivitySearchBinding

    private var isListView: Boolean = true


    private val imagesList: ArrayList<ImgurModel> = ArrayList()
    private lateinit var adapter: SearchResultsAdapter

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        val appComponent = (application as BaseApplication).appComponent
        appComponent.inject(this)
        searchViewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
        changeLayout()
        setUpListener()
        setUpObserver()


    }

    private fun setUpObserver() {
        searchViewModel.uiState.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    showShimmer()
                    disableInput()

                }
                is UiState.Success -> {
                    enableInput()
                    hideShimmer()
                    if (it.response?.data?.isNotEmpty() == true) {
                        imagesList.clear()
                        imagesList.addAll(it.response?.data)
                        refreshAdapter()
                    } else {
                        //No images from api
                        showNoImagesFoundLayout()
                    }
                }
                is UiState.Error -> {
                    enableInput()
                    hideShimmer()
                    showImageLoadFailedLayout()
                }
            }

        }
    }

    private fun showNoImagesFoundLayout() {
        mBinding.recyclerImages.visibility = View.GONE
        mBinding.errorLayout.apply {
            errorContainer.visibility = View.VISIBLE
            tvErrorMsg.text = "No images found for ${mBinding.edQuery.text.toString().trim()}"
            ivErrorImage.setImageResource(R.drawable.ic_not_found)
            btnRetry.visibility = View.GONE
        }
    }

    private fun showImageLoadFailedLayout() {
        mBinding.recyclerImages.visibility = View.GONE
        mBinding.errorLayout.apply {
            errorContainer.visibility = View.VISIBLE
            tvErrorMsg.text = "Failed to load image for  ${mBinding.edQuery.text.toString().trim()}"
            ivErrorImage.setImageResource(R.drawable.ic_error)
            btnRetry.visibility = View.VISIBLE
        }
    }

    private fun enableInput() {
        mBinding.edQuery.isEnabled = true
        mBinding.icListType.isEnabled = true
    }

    private fun disableInput() {
        mBinding.edQuery.isEnabled = false
        mBinding.icListType.isEnabled = false
    }


    private fun refreshAdapter() {
        if (::adapter.isInitialized) {
            adapter.refreshData(imagesList, isListView)
        } else {
            adapter = SearchResultsAdapter(this, imagesList, isListView)
            mBinding.recyclerImages.adapter = adapter
        }
        mBinding.recyclerImages.layoutManager = mLayoutManager


    }

    private fun changeLayout() {
        while (mBinding.recyclerImages.itemDecorationCount > 0) {
            mBinding.recyclerImages.removeItemDecorationAt(0);
        }
        if (isListView) {
            mBinding.icListType.setImageDrawable(
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.ic_view_list
                )
            )
            mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        } else {
            mBinding.icListType.setImageDrawable(
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.ic_view_grid
                )
            )
            mLayoutManager = GridLayoutManager(this, 2)

            mBinding.recyclerImages.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    20,
                    true
                )
            )

        }
    }

    private fun showShimmer() {
        mBinding.recyclerImages.visibility = View.GONE
        mBinding.errorLayout.errorContainer.visibility = View.GONE
        if (isListView) {
            mBinding.shimmerListContainer.shimmerList.apply {
                visibility = View.VISIBLE
                mBinding.shimmerListContainer.shimmerList.startShimmer()
            }
        } else {
            mBinding.shimmerGridContainer.shimmerGrid.apply {
                visibility = View.VISIBLE
                mBinding.shimmerListContainer.shimmerList.startShimmer()
            }
        }
    }

    private fun hideShimmer() {
        mBinding.recyclerImages.visibility = View.VISIBLE
        mBinding.shimmerGridContainer.shimmerGrid.apply {
            visibility = View.GONE
            mBinding.shimmerGridContainer.shimmerGrid.stopShimmer()
        }
        mBinding.shimmerListContainer.shimmerList.apply {
            visibility = View.GONE
            mBinding.shimmerListContainer.shimmerList.stopShimmer()

        }

    }

    private fun setUpListener() {
        mBinding.edQuery.doAfterTextChanged {
            val input = it.toString().trim()
            if (!TextUtils.isEmpty(input) && input.length > 2) {
                /**
                 * Start search on atleast 3 chars written by user.
                 */
                searchViewModel.searchImages(SearchRequestModel(q = input))

            }

        }
        mBinding.icListType.setOnClickListener {
            isListView = mBinding.recyclerImages.layoutManager is GridLayoutManager
            changeLayout()
            refreshAdapter()
        }

        mBinding.errorLayout.btnRetry.setOnClickListener {
            searchViewModel.searchImages(
                SearchRequestModel(
                    q = mBinding.edQuery.text.toString().trim()
                )
            )
        }

    }
}