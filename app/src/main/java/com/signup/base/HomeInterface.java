package com.signup.base;

import com.signup.model.login.AppUser;

public interface HomeInterface
        extends HostActivityInterface,
        DrawerActivityInterface {
    AppUser getUser();
}