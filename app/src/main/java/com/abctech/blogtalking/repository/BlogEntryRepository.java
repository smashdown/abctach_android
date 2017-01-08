package com.abctech.blogtalking.repository;

import com.abctech.blogtalking.event.BTEventBlogEntryChanged;
import com.abctech.blogtalking.model.BTBlogEntry;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class BlogEntryRepository extends BaseRealmRepository<BTBlogEntry> {

    public BlogEntryRepository(Realm realm) {
        super(realm);
    }

    @Override
    public void save(final BTBlogEntry item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
                EventBus.getDefault().post(new BTEventBlogEntryChanged());
            }
        });
    }

    @Override
    public void save(final Iterable<BTBlogEntry> items) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(items);
                EventBus.getDefault().post(new BTEventBlogEntryChanged());
            }
        });
    }

    @Override
    public long count() {
        return realm.where(BTBlogEntry.class).count();
    }

    @Override
    public void update(final BTBlogEntry item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(BTBlogEntry.class).equalTo("id", item.getId()).findAll().deleteAllFromRealm();
                realm.copyToRealm(item);

                EventBus.getDefault().post(new BTEventBlogEntryChanged());
            }
        });
    }

    @Override
    public void delete(final BTBlogEntry item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(BTBlogEntry.class).equalTo("id", item.getId()).findAll().deleteAllFromRealm();
                EventBus.getDefault().post(new BTEventBlogEntryChanged());
            }
        });
    }

    @Override
    public void deleteAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(BTBlogEntry.class);
                EventBus.getDefault().post(new BTEventBlogEntryChanged());
            }
        });
    }

    @Override
    public List<BTBlogEntry> findAll() {
        return realm.where(BTBlogEntry.class).findAll();
    }

    public List<BTBlogEntry> findAllSortedByDated() {
        return realm.copyFromRealm(realm.where(BTBlogEntry.class).findAllSorted("createdDate", Sort.DESCENDING));
    }

    public List<BTBlogEntry> findAllSortedByTitleAsc() {
        return realm.copyFromRealm(realm.where(BTBlogEntry.class).findAllSorted("title", Sort.ASCENDING));
    }

    public List<BTBlogEntry> findAllSortedByTitleDesc() {
        return realm.copyFromRealm(realm.where(BTBlogEntry.class).findAllSorted("title", Sort.DESCENDING));
    }

    public BTBlogEntry findById(String id) {
        return realm.where(BTBlogEntry.class).equalTo("id", id).findFirst();
    }
}