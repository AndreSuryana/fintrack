package com.andresuryana.fintrack.ui.profile.setting;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.andresuryana.fintrack.R;

public enum MenuSetting {

    // Parent - MANAGE
    MANAGE_CATEGORIES("manage_category", Parent.PARENT_MANAGE, R.string.title_manage_category, R.drawable.ic_category_filled),

    // Parent - ACCOUNT SETTING
    PERSONAL_DATA("personal_data", Parent.PARENT_ACCOUNT_SETTING, R.string.title_personal_data, R.drawable.ic_profile_filled),
    FORGOT_PASSWORD("forgot_password", Parent.PARENT_ACCOUNT_SETTING, R.string.title_forgot_password, R.drawable.ic_lock),
    LOGOUT("logout", Parent.PARENT_ACCOUNT_SETTING, R.string.title_logout, R.drawable.ic_logout);

    private String id;
    private Parent parent;
    private @StringRes Integer title;
    private @DrawableRes Integer icon;

    MenuSetting(String id, Parent parent, @StringRes Integer title, @DrawableRes Integer icon) {
        this.id = id;
        this.parent = parent;
        this.title = title;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }


    // Parent Menu
    public enum Parent {
        PARENT_MANAGE(1, R.string.title_manage),
        PARENT_ACCOUNT_SETTING(2, R.string.title_account_setting);

        private Integer id;
        private @StringRes Integer title;

        Parent(Integer id, @StringRes Integer title) {
            this.id = id;
            this.title = title;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTitle() {
            return title;
        }

        public void setTitle(Integer title) {
            this.title = title;
        }
    }
}
