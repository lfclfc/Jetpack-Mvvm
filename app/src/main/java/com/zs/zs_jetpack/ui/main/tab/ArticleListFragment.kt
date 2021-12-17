package com.zs.zs_jetpack.ui.main.tab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zs.base_library.base.BaseVmFragment
import com.zs.base_library.base.DataBindingConfig
import com.zs.base_library.base.LazyVmFragment
import com.zs.base_library.common.smartConfig
import com.zs.base_library.common.smartDismiss
import com.zs.base_wa_lib.base.BaseLazyLoadingFragment
import com.zs.zs_jetpack.BR
import com.zs.zs_jetpack.R
import com.zs.zs_jetpack.common.ArticleAdapter
import com.zs.zs_jetpack.databinding.FragmentArticleBinding
import com.zs.zs_jetpack.databinding.FragmentArticleBindingImpl
import com.zs.zs_jetpack.utils.CacheUtil
import kotlinx.android.synthetic.main.fragment_article.*

/**
 * des 文章列表fragment
 * @date 2020/7/7
 * @author zs
 */
class ArticleListFragment : BaseVmFragment<FragmentArticleBinding>() {

    private var articleVM: ArticleVM? = null

    /**
     * fragment类型，项目或公号
     */
    private var type = 0

    /**
     * tab的id
     */
    private var tabId = 0

    /**
     * 文章适配器
     */
    private val adapter by lazy { ArticleAdapter(mActivity) }

    override fun initFragmentViewModel() {
        articleVM = getFragmentViewModel(ArticleVM::class.java)
    }

    override fun observe() {
        articleVM?.articleLiveData?.observe(this, Observer {
            smartRefresh.smartDismiss()
            loadingTip.dismiss()
            adapter.submitList(it)
        })
        articleVM?.errorLiveData?.observe(this, Observer {
            smartRefresh.smartDismiss()
            if (it.errorCode == -100) {
                //显示网络错误
                loadingTip.showInternetError()
                loadingTip.setReloadListener {
                    articleVM?.getArticleList(type, tabId)
                }
            }
        })
    }

    override fun init(savedInstanceState: Bundle?) {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
        initView()
        loadData()
    }
    override fun initView() {
        //关闭更新动画
        (rvArticleList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        //下拉刷新
        smartRefresh.setOnRefreshListener {
            articleVM?.getArticleList(type, tabId)
        }
        //上拉加载
        smartRefresh.setOnLoadMoreListener {
            articleVM?.loadMoreArticleList(type,tabId)
        }
        smartRefresh.smartConfig()
        adapter.apply {
            rvArticleList.adapter = this
            setOnItemClickListener { i, _ ->
                nav().navigate(
                    R.id.action_main_fragment_to_web_fragment,
                    this@ArticleListFragment.adapter.getBundle(i)
                )
            }
            setOnItemChildClickListener { i, view ->
                when (view.id) {
                    //收藏
                    R.id.ivCollect -> {
                        if (CacheUtil.isLogin()) {
                            this@ArticleListFragment.adapter.currentList[i].apply {
                                //已收藏取消收藏
                                if (collect) {
                                    articleVM?.unCollect(id)
                                } else {
                                    articleVM?.collect(id)
                                }
                            }
                        } else {
                            nav().navigate(R.id.action_main_fragment_to_login_fragment)
                        }
                    }
                }
            }
        }
    }

    override fun loadData() {
        articleVM?.getArticleList(type, tabId)
        loadingTip.loading()
    }

    override fun getLayoutId() = R.layout.fragment_article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("zszs","onCreate")

    }

    override fun onStart() {
        super.onStart()
        Log.i("zszs","onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.i("zszs","onResume")

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("zszs","onAttach")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("zszs","onCreateView")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("zszs","onViewCreated")

    }

    override fun onPause() {
        super.onPause()
        Log.i("zszs","onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.i("zszs","onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("zszs","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("zszs","onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("zszs","onDetach")

    }
}