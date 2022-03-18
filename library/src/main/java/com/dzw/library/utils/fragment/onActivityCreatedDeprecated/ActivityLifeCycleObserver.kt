package com.dzw.library.utils.fragment.onActivityCreatedDeprecated

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author Death丶Love
 * @date 2022-03-18 11:00
 * @description 由于 google 废除了 fragment 中 onActivityCreated()
 * 导致很多需要在 onActivityCreated() 操作的内容由于生命周期不走此处而无法使用
 * 而 google 推荐使用 liftCycle 进行 fragment 的生命周期管理
 * 因此使用此类进行替代 onActivityCreated()
 */
class ActivityLifeCycleObserver(private val block: () -> Unit) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.removeObserver(this)
        block.invoke()
    }
}