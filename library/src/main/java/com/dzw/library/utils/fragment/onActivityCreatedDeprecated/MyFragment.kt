package com.dzw.library.utils.fragment.onActivityCreatedDeprecated

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @author Death丶Love
 * @date 2022-03-18 11:52
 * @description 在 fragment 的 onAttach() 方法中处理原本需要在 onActivityCreated() 处理的内容
 */
class MyFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(ActivityLifeCycleObserver {
            //do something you want
        })
    }
}