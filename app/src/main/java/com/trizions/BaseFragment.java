package com.trizions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.trizions.dialog.CustomDialog;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    public static boolean isForeground;
    protected static final String DIALOG_TAG = "DIALOG_TAG";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        try {
            if (view != null) {
                ButterKnife.bind(this, view);
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
        return view;
    }

    protected View createView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return null;
    }

    protected void showForegroundToast(String text) {
        try {
            if (isForeground) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    protected boolean isForeground() {
        return isForeground;
    }


    protected void showCustomDialog(String title, String message, String action, String status, CustomDialog.OnDismissListener listener) {
        try {
            CustomDialog customDialog = CustomDialog.newInstance(title, message, action, status);
            customDialog.setListener(null);
            if(listener != null) {
                customDialog.setListener(listener);
            }
            getActivity().getFragmentManager().beginTransaction()
                    .add(customDialog, DIALOG_TAG)
                    .commitAllowingStateLoss();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean isNetworkConnectionAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) return false;
            NetworkInfo.State network = info.getState();
            return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
        return false;
    }
}
