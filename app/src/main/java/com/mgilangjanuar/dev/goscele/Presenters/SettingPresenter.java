package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.widget.ToggleButton;

import com.mgilangjanuar.dev.goscele.Adapters.SettingAdapter;
import com.mgilangjanuar.dev.goscele.AuthActivity;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Models.AccountModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class SettingPresenter {

    private ArrayList<Map<String, String>> listContent;

    private Activity activity;

    public SettingPresenter(Activity activity) {
        this.activity = activity;
        listContent = new ArrayList<>();
    }

    public SettingAdapter buildAdapter() {
        if (listContent.isEmpty()) {
            buildList();
        }

        return new SettingAdapter(activity, listContent, this);
    }

    public void buildList() {
        final AccountModel accountModel = new AccountModel(activity);

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "PROFILE INFO");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Full Name");
            put("content", accountModel.getSavedName());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Username");
            put("content", accountModel.getSavedUsername());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "ACCOUNT SETTINGS");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Logout");
            put("content", "Logout from " + accountModel.getSavedUsername());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "In-App Browser");
            put("content", "Enable in-app browser?");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Save Password");
            put("content", "Allow application store password?");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "ABOUT");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Application Name");
            put("content", activity.getResources().getString(R.string.app_name));
        }});

        String version = "";
        try {
            PackageManager manager = activity.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(
                    activity.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        final String versionName = version;
        listContent.add(new HashMap<String, String>() {{
            put("title", "Application Version");
            put("content", versionName);
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Rate and Feedback");
            put("content", "Give feedback and rate this app");
        }});
    }

    public void logoutActionHelper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            AuthPresenter authPresenter = new AuthPresenter(activity);
            authPresenter.showProgressDialog();
            (new Thread(() -> {
                boolean isLogout = authPresenter.logout();
                activity.runOnUiThread(() -> {
                    if (isLogout) {
                        ((MainActivity) activity).forceRedirect(new Intent(activity, AuthActivity.class));
                    }
                    authPresenter.dismissProgressDialog();
                });
            })).start();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
        });
        alert.show();
    }

    public void savePasswordActionHelper(final ToggleButton toggle) {
        toggle.setChecked(!toggle.isChecked());
        final AccountModel accountModel = new AccountModel(activity);
        String message;
        if (accountModel.isSaveCredential()) {
            message = "The application will remove your password. Want to continue?";
        } else {
            message = "The application needs re-authentication for password saving. Want to continue?";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Save Password");
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            accountModel.toggleSaveCredential();
            if (accountModel.isSaveCredential()) {
                ((MainActivity) activity).forceRedirect(new Intent(activity, AuthActivity.class));
                toggle.setChecked(true);
            } else {
                ((BaseActivity) activity).showToast("Password removed");
                toggle.setChecked(false);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
        });
        alert.show();
    }

    public interface SettingServicePresenter {
        void setupContents(View view);
    }

}
