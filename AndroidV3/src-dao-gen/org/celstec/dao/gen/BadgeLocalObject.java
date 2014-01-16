package org.celstec.dao.gen;

import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table BADGE_LOCAL_OBJECT.
 */
public class BadgeLocalObject {

    private Long id;
    private String title;
    private String description;
    private byte[] badgeIcon;
    private Long inquiryId;
    private long accountId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient BadgeLocalObjectDao myDao;

    private InquiryLocalObject inquiryLocalObject;
    private Long inquiryLocalObject__resolvedKey;

    private AccountLocalObject accountLocalObject;
    private Long accountLocalObject__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public BadgeLocalObject() {
    }

    public BadgeLocalObject(Long id) {
        this.id = id;
    }

    public BadgeLocalObject(Long id, String title, String description, byte[] badgeIcon, Long inquiryId, long accountId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.badgeIcon = badgeIcon;
        this.inquiryId = inquiryId;
        this.accountId = accountId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBadgeLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(byte[] badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    public Long getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(Long inquiryId) {
        this.inquiryId = inquiryId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /** To-one relationship, resolved on first access. */
    public InquiryLocalObject getInquiryLocalObject() {
        Long __key = this.inquiryId;
        if (inquiryLocalObject__resolvedKey == null || !inquiryLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InquiryLocalObjectDao targetDao = daoSession.getInquiryLocalObjectDao();
            InquiryLocalObject inquiryLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                inquiryLocalObject = inquiryLocalObjectNew;
            	inquiryLocalObject__resolvedKey = __key;
            }
        }
        return inquiryLocalObject;
    }

    public void setInquiryLocalObject(InquiryLocalObject inquiryLocalObject) {
        synchronized (this) {
            this.inquiryLocalObject = inquiryLocalObject;
            inquiryId = inquiryLocalObject == null ? null : inquiryLocalObject.getId();
            inquiryLocalObject__resolvedKey = inquiryId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public AccountLocalObject getAccountLocalObject() {
        long __key = this.accountId;
        if (accountLocalObject__resolvedKey == null || !accountLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountLocalObjectDao targetDao = daoSession.getAccountLocalObjectDao();
            AccountLocalObject accountLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                accountLocalObject = accountLocalObjectNew;
            	accountLocalObject__resolvedKey = __key;
            }
        }
        return accountLocalObject;
    }

    public void setAccountLocalObject(AccountLocalObject accountLocalObject) {
        if (accountLocalObject == null) {
            throw new DaoException("To-one property 'accountId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.accountLocalObject = accountLocalObject;
            accountId = accountLocalObject.getId();
            accountLocalObject__resolvedKey = accountId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}