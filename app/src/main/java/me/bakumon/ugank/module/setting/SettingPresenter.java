package me.bakumon.ugank.module.setting;

import android.content.Context;
import android.support.annotation.NonNull;

import me.bakumon.ugank.App;
import me.bakumon.ugank.ConfigManage;
import me.bakumon.ugank.ThemeManage;
import me.bakumon.ugank.utills.DataCleanManager;
import me.bakumon.ugank.utills.PackageUtil;
import rx.subscriptions.CompositeSubscription;

/**
 * SettingPresenter
 * Created by bakumon on 2016/12/15 17:08.
 */
public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    public SettingPresenter(SettingContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
        mView.setSwitchCompatsColor(ThemeManage.INSTANCE.getColorPrimary());
        mView.setToolbarBackgroundColor(ThemeManage.INSTANCE.getColorPrimary());
        // 初始化开关显示状态
        mView.changeSwitchState(ConfigManage.INSTANCE.isListShowImg());
        setImageQualityChooseIsEnable(ConfigManage.INSTANCE.isListShowImg());
        mView.setAppVersionNameInTv(PackageUtil.getVersionName(App.getInstance()));
        setThumbnailQuality(ConfigManage.INSTANCE.getThumbnailQuality());
        try {
            mView.showCacheSize(DataCleanManager.getTotalCacheSize((Context) mView));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setThumbnailQualityInfo(int quality) {
        String thumbnailQuality = "";
        switch (quality) {
            case 0:
                thumbnailQuality = "原图";
                break;
            case 1:
                thumbnailQuality = "默认";
                break;
            case 2:
                thumbnailQuality = "省流";
                break;
        }
        mView.setThumbnailQualityInfo(thumbnailQuality);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void saveIsListShowImg(boolean isListShowImg) {
        ConfigManage.INSTANCE.setListShowImg(isListShowImg);
        setImageQualityChooseIsEnable(isListShowImg);
    }

    private void setImageQualityChooseIsEnable(boolean isEnable) {
        if (isEnable) {
            mView.setImageQualityChooseEnable();
        } else {
            mView.setImageQualityChooseUnEnable();
        }
    }

    @Override
    public int getColorPrimary() {
        return ThemeManage.INSTANCE.getColorPrimary();
    }

    @Override
    public int getThumbnailQuality() {
        return ConfigManage.INSTANCE.getThumbnailQuality();
    }

    @Override
    public void setThumbnailQuality(int quality) {
        ConfigManage.INSTANCE.setThumbnailQuality(quality);
        setThumbnailQualityInfo(quality);
    }

    @Override
    public void cleanCache(Context context) {
        DataCleanManager.clearAllCache(context);
        try {
            mView.showCacheSize(DataCleanManager.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
