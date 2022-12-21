package com.thoughtctl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtctl.adapter.SearchResultsAdapter
import com.thoughtctl.api.Status
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
        searchViewModel.imagesLiveData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showShimmer()
                    disableInput()

                }
                Status.SUCCESS -> {
                    enableInput()
                    hideShimmer()
                    val response = it.data as? ImgurResponseModel
                    if (response?.data?.isNotEmpty() == true) {
                        imagesList.clear()
                        imagesList.addAll(response?.data ?: ArrayList())
                        refreshAdapter()
                    } else {
                        //No images from api
                    }
                }
                Status.ERROR -> {
                    enableInput()
                    hideShimmer()
                    showErrorToast(it.error)
                }
            }

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

    private fun showErrorToast(error: Throwable?) {
        Toast.makeText(this, "Failed to load images, please try after sometime", Toast.LENGTH_LONG)
            .show()
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


    }
}