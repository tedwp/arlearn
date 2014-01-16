package org.celstec.dao.gen;

import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GAME_CONTRIBUTOR_LOCAL_OBJECT.
 */
public class GameContributorLocalObject {

    private Long id;
    private Integer type;
    private long gameId;
    private long accountId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GameContributorLocalObjectDao myDao;

    private GameLocalObject gameLocalObject;
    private Long gameLocalObject__resolvedKey;

    private AccountLocalObject accountLocalObject;
    private Long accountLocalObject__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GameContributorLocalObject() {
    }

    public GameContributorLocalObject(Long id) {
        this.id = id;
    }

    public GameContributorLocalObject(Long id, Integer type, long gameId, long accountId) {
        this.id = id;
        this.type = type;
        this.gameId = gameId;
        this.accountId = accountId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGameContributorLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /** To-one relationship, resolved on first access. */
    public GameLocalObject getGameLocalObject() {
        long __key = this.gameId;
        if (gameLocalObject__resolvedKey == null || !gameLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GameLocalObjectDao targetDao = daoSession.getGameLocalObjectDao();
            GameLocalObject gameLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                gameLocalObject = gameLocalObjectNew;
            	gameLocalObject__resolvedKey = __key;
            }
        }
        return gameLocalObject;
    }

    public void setGameLocalObject(GameLocalObject gameLocalObject) {
        if (gameLocalObject == null) {
            throw new DaoException("To-one property 'gameId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gameLocalObject = gameLocalObject;
            gameId = gameLocalObject.getId();
            gameLocalObject__resolvedKey = gameId;
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