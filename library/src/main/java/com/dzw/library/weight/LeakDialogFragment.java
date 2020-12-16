package com.dzw.library.weight;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

/**
 * @author Death丶Love
 * @date 2020年12月16日16:45:17
 * @description 解决 dialogFragment 内存泄露问题
 * 某些场景 dialogfragment 中的某些操作在监听回调方法中会持有 context
 * 此时会造成内存泄漏
 */
public class LeakDialogFragment extends DialogFragment {
    public static class DialogDismissListener implements DialogInterface.OnDismissListener {
        private WeakReference<LeakDialogFragment> leakDialogFragmentWeakReference;

        DialogDismissListener(LeakDialogFragment leakDialogFragment) {
            this.leakDialogFragmentWeakReference = new WeakReference<>(leakDialogFragment);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            LeakDialogFragment leakDialogFragment = leakDialogFragmentWeakReference.get();
            if (leakDialogFragment != null) {
                leakDialogFragment.onDismiss(dialog);
            }
        }
    }

    public static class DialogCancelListener implements DialogInterface.OnCancelListener {
        private WeakReference<LeakDialogFragment> leakDialogFragmentWeakReference;

        DialogCancelListener(LeakDialogFragment leakDialogFragment) {
            this.leakDialogFragmentWeakReference = new WeakReference<>(leakDialogFragment);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            LeakDialogFragment leakDialogFragment = leakDialogFragmentWeakReference.get();
            if (leakDialogFragment != null) {
                leakDialogFragment.onCancel(dialog);
            }
        }
    }

    /**
     * 解决原生setOnDismissListener和setOnCancelListener内存泄露问题
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        if (getShowsDialog()) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
        setShowsDialog(true);

        if (getDialog() == null) {
            return;
        }

        android.view.View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            getDialog().setOwnerActivity(activity);
        }
        getDialog().setCancelable(isCancelable());
        LeakDialogFragment.DialogDismissListener mDialogDissmissLitener = new LeakDialogFragment.DialogDismissListener(this);//设置一个自定义的DissmissListener
        getDialog().setOnDismissListener(mDialogDissmissLitener);
        LeakDialogFragment.DialogCancelListener mDialogCancelListener = new LeakDialogFragment.DialogCancelListener(this);//设置一个自定义的DissmissListener
        getDialog().setOnCancelListener(mDialogCancelListener);
        if (savedInstanceState != null) {
            android.os.Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null) {
                getDialog().onRestoreInstanceState(dialogState);
            }
        }
    }
}