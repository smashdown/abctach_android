package com.abctech.blogtalking.repository;

import io.realm.Realm;
import io.realm.RealmObject;

public abstract class BaseRealmRepository<T extends RealmObject> implements BaseRepository<T> {
    protected Realm realm;

    public BaseRealmRepository(Realm realm) {
        this.realm = realm;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }
}