package marian.vasilca.videoplayer.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import marian.vasilca.videoplayer.utilities.AutoClearedValue

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    internal abstract val layoutResource: Int

    // tag is not a companion object because it should be used only from a BaseFragment reference
    internal abstract val tag: String

    private lateinit var viewDataBinding: T
    @VisibleForTesting
    var binding: AutoClearedValue<T>? = null

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(tag, "onCreateView")
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
        binding = AutoClearedValue(this)
        return viewDataBinding.root
    }

    /**
     * Using Kotlin Extensions we can achieve View Binding after view is created. Make sure that
     * every view method call is done after this method is called.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(tag, "onViewCreated")
        onBoundViews(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    internal abstract fun onBoundViews(savedInstanceState: Bundle?)
}