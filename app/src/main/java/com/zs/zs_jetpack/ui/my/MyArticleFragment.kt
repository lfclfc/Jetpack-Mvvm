package com.zs.zs_jetpack.ui.my

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zs.base_library.base.BaseVmFragment
import com.zs.base_library.common.clickNoRepeat
import com.zs.base_library.common.smartDismiss
import com.zs.base_library.common.toast
import com.zs.zs_jetpack.R
import com.zs.zs_jetpack.common.OnChildItemClickListener
import com.zs.zs_jetpack.constants.Constants
import com.zs.zs_jetpack.utils.CacheUtil
import com.zs.base_wa_lib.view.LoadingTip
import com.zs.zs_jetpack.databinding.FragmentMyArticleBinding
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * des 我的文章
 * @author zs
 * @data 2020/7/12
 */
class MyArticleFragment : BaseVmFragment<FragmentMyArticleBinding>(), OnChildItemClickListener {

    private val adapter by lazy { MyArticleAdapter() }
    private lateinit var myVM: MyArticleVM

    private val loadingView by lazy {
        LoadingTip(
            mActivity
        )
    }

    override fun initViewModel() {
        myVM = getFragmentViewModel(MyArticleVM::class.java)
    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        loadData()
    }

    override fun initView() {
        adapter.apply {
            setOnChildItemClickListener(this@MyArticleFragment)
            emptyView = loadingView
            binding.rvMyArticleList.adapter = this
        }
        binding.smartRefresh.setOnRefreshListener {
            myVM.getMyArticle()
        }
        binding.smartRefresh.setOnLoadMoreListener {
            myVM.loadMore()
        }
    }

    override fun onClick() {
        binding.ivBack.clickNoRepeat {
            nav().navigateUp()
        }
        binding.ivAdd.clickNoRepeat {
            if (CacheUtil.isLogin()) {
                nav().navigate(R.id.action_my_article_fragment_to_publish_fragment)
            } else {
                toast("请先登录～")
            }
        }
    }

    override fun observe() {
        myVM.myLiveDate.observe(this, Observer {
            binding.smartRefresh.smartDismiss()
            adapter.setNewData(it)
        })
        myVM.deleteLiveData.observe(this, Observer {
            adapter.deleteById(it)
        })
        myVM.emptyLiveDate.observe(this, Observer {
            loadingView.showEmpty()
        })
        myVM.errorLiveData.observe(this, Observer {
            binding.smartRefresh.smartDismiss()
        })
    }

    override fun loadData() {
        binding.smartRefresh.autoRefresh()
    }

    override fun getLayoutId() = R.layout.fragment_my_article

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        this.adapter.data.apply {
            when(view.id){
                R.id.rlContent->{
                    nav().navigate(R.id.action_my_article_fragment_to_web_fragment,Bundle().apply {
                        putString(Constants.WEB_URL,get(position).link)
                        putString(Constants.WEB_TITLE,get(position).title)
                    })
                }
                R.id.tvDelete->{
                    if (position<size){
                        myVM.delete(get(position).id)
                    }
                }
            }
        }
    }
}