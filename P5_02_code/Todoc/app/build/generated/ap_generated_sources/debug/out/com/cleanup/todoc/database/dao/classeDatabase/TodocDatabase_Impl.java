package com.cleanup.todoc.database.dao.classeDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.ProjectDao_Impl;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.dao.TaskDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class TodocDatabase_Impl extends TodocDatabase {
  private volatile ProjectDao _projectDao;

  private volatile TaskDao _taskDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `project` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `task` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `projectId` INTEGER NOT NULL, `name` TEXT NOT NULL, `creationTimestamp` INTEGER NOT NULL, FOREIGN KEY(`projectId`) REFERENCES `project`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"fc206f1944a529fdcfa01a9d988d5bb2\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `project`");
        _db.execSQL("DROP TABLE IF EXISTS `task`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsProject = new HashMap<String, TableInfo.Column>(3);
        _columnsProject.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsProject.put("name", new TableInfo.Column("name", "TEXT", true, 0));
        _columnsProject.put("color", new TableInfo.Column("color", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProject = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProject = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProject = new TableInfo("project", _columnsProject, _foreignKeysProject, _indicesProject);
        final TableInfo _existingProject = TableInfo.read(_db, "project");
        if (! _infoProject.equals(_existingProject)) {
          throw new IllegalStateException("Migration didn't properly handle project(com.cleanup.todoc.model.Project).\n"
                  + " Expected:\n" + _infoProject + "\n"
                  + " Found:\n" + _existingProject);
        }
        final HashMap<String, TableInfo.Column> _columnsTask = new HashMap<String, TableInfo.Column>(4);
        _columnsTask.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsTask.put("projectId", new TableInfo.Column("projectId", "INTEGER", true, 0));
        _columnsTask.put("name", new TableInfo.Column("name", "TEXT", true, 0));
        _columnsTask.put("creationTimestamp", new TableInfo.Column("creationTimestamp", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTask = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTask.add(new TableInfo.ForeignKey("project", "NO ACTION", "NO ACTION",Arrays.asList("projectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTask = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTask = new TableInfo("task", _columnsTask, _foreignKeysTask, _indicesTask);
        final TableInfo _existingTask = TableInfo.read(_db, "task");
        if (! _infoTask.equals(_existingTask)) {
          throw new IllegalStateException("Migration didn't properly handle task(com.cleanup.todoc.model.Task).\n"
                  + " Expected:\n" + _infoTask + "\n"
                  + " Found:\n" + _existingTask);
        }
      }
    }, "fc206f1944a529fdcfa01a9d988d5bb2", "0ea83747103ac85511377f295918cdbe");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "project","task");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `task`");
      _db.execSQL("DELETE FROM `project`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public ProjectDao mProjectDao() {
    if (_projectDao != null) {
      return _projectDao;
    } else {
      synchronized(this) {
        if(_projectDao == null) {
          _projectDao = new ProjectDao_Impl(this);
        }
        return _projectDao;
      }
    }
  }

  @Override
  public TaskDao mTaskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
