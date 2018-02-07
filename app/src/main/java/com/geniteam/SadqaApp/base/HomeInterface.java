package com.geniteam.SadqaApp.base;

import com.geniteam.SadqaApp.model.login.AppUser;

public interface HomeInterface
        extends HostActivityInterface,
        DrawerActivityInterface {
    AppUser getUser();
}