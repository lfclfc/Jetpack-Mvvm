package com.zs.zs_jetpack.ui.main.mine


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zs.base_library.base.LazyVmFragment
import com.zs.base_library.common.clickNoRepeat
import com.zs.base_library.common.toast
import com.zs.zs_jetpack.R
import com.zs.zs_jetpack.constants.Constants
import com.zs.zs_jetpack.constants.UrlConstants
import com.zs.zs_jetpack.databinding.FragmentMineBinding
import com.zs.zs_jetpack.event.LoginEvent
import com.zs.zs_jetpack.event.LogoutEvent
import com.zs.zs_jetpack.utils.CacheUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * des 我的
 * @author zs
 * @date 2020-05-14
 */
class MineFragment : LazyVmFragment<FragmentMineBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private var mineVM: MineVM? = null

    override fun initViewModel() {
        mineVM = getFragmentViewModel(MineVM::class.java)
    }

    override fun lazyInit() {
        binding.vm = mineVM
        mineVM?.getInternal()
    }

    override fun getLayoutId() = R.layout.fragment_mine

    override fun onClick() {
        binding.ivHead.clickNoRepeat {
            toast("我只是一只睡着的小老鼠...")
        }
        binding.tvName.clickNoRepeat {
            if (!CacheUtil.isLogin()) {
                nav().navigate(R.id.action_main_fragment_to_login_fragment)
            }
        }
        binding.llHistory.clickNoRepeat {
            nav().navigate(R.id.action_main_fragment_to_history_fragment)
        }
        binding.llRanking.clickNoRepeat {
            if (CacheUtil.isLogin()) {
                val integralBean = mineVM?.internalLiveData?.value
                nav().navigate(R.id.action_main_fragment_to_rank_fragment, Bundle().apply {
                    integralBean?.apply {
                        putInt(Constants.MY_INTEGRAL, coinCount)
                        putInt(Constants.MY_RANK, rank)
                        putString(Constants.MY_NAME, username)
                    }
                })
            } else {
                toast("请先登录")
            }
        }
        binding.clIntegral.clickNoRepeat {
            if (CacheUtil.isLogin()) {
                nav().navigate(R.id.action_main_fragment_to_integral_fragment)
            } else {
                toast("请先登录")
            }
        }
        binding.clCollect.clickNoRepeat {
            if (CacheUtil.isLogin()) {
                nav().navigate(R.id.action_main_fragment_to_my_article_fragment)
            } else {
                toast("请先登录")
            }
        }
        binding.clArticle.clickNoRepeat {
            if (CacheUtil.isLogin()) {
                nav().navigate(R.id.action_main_fragment_to_my_article_fragment)
            } else {
                toast("请先登录")
            }
        }
        binding.clWebsite.clickNoRepeat {
            nav().navigate(R.id.action_main_fragment_to_web_fragment, Bundle().apply {
                putString(Constants.WEB_URL, UrlConstants.WEBSITE)
                putString(Constants.WEB_TITLE, Constants.APP_NAME)
            })
        }
        binding.clSet.clickNoRepeat {
            nav().navigate(R.id.action_main_fragment_to_set_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 登陆消息,收到消息请求个人信息接口
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(loginEvent: LoginEvent) {
        mineVM?.getInternal()
    }

    /**
     * 退出消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun logoutEvent(loginEvent: LogoutEvent) {
        mineVM?.username?.set("请先登录")
        mineVM?.id?.set("---")
        mineVM?.rank?.set("0")
        mineVM?.internal?.set("0")
    }
}
