package com.thoughtctl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private lateinit var searchViewModel: SearchViewModel

    private lateinit var mBinding: ActivitySearchBinding


    private val imagesList: ArrayList<ImgurModel> = ArrayList()
    private lateinit var adapter: SearchResultsAdapter

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        val appComponent = (application as BaseApplication).appComponent
        appComponent.inject(this)
        searchViewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
        changeLayout(true)
        setUpListener()
        setUpObserver()


    }

    private fun setUpObserver() {
        searchViewModel.imagesLiveData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showShimmer()

                }
                Status.SUCCESS -> {
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
                    hideShimmer()
                    showErrorToast(it.error)
                }
            }

        }
    }

    private fun showErrorToast(error: Throwable?) {
        Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
    }

    private fun refreshAdapter() {
        if (::adapter.isInitialized) {
            adapter.refreshData(imagesList)
        } else {
            adapter = SearchResultsAdapter(this, imagesList)
            mBinding.recyclerImages.adapter = adapter
        }
        mBinding.recyclerImages.layoutManager = mLayoutManager


    }

    private fun changeLayout(isListView: Boolean) {
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
        }
    }

    private fun showShimmer() {

    }

    private fun hideShimmer() {

    }

    private fun setUpListener() {
        mBinding.edQuery.doAfterTextChanged {
            val input = it.toString().trim()
            if (!TextUtils.isEmpty(input) && input.length > 2) {
                searchViewModel.searchImages(SearchRequestModel(q = input))

            }

        }
        mBinding.icListType.setOnClickListener {
            if (mBinding.recyclerImages.layoutManager is GridLayoutManager) {
                changeLayout(true)
            } else {
                changeLayout(false)

            }
            refreshAdapter()
        }


    }
}