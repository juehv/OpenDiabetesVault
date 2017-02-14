/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.data;

import com.j256.ormlite.dao.CloseableIterator;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.db.HsqldbDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class VaultDao {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem:odvault";
    private static final Logger LOG = Logger.getLogger(VaultDao.class.getName());
    private static VaultDao INSTANCE = null;

    private ConnectionSource connectionSource;
    private Dao<VaultEntry, Long> vaultDao;

    private VaultDao() {
    }

    public static VaultDao getInstance() {
        if (INSTANCE == null) {
            LOG.severe("Database is not initialized. Call VaultDao.initializeDb first!");
            System.exit(-1);
        }
        return INSTANCE;
    }

    public static void finalizeDb() throws IOException {
        INSTANCE.connectionSource.close();
    }

    public static void initializeDb() throws SQLException {
        //TODO combine logging
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
        INSTANCE = new VaultDao();
        INSTANCE.initDb();
    }

    private void initDb() throws SQLException {
        // create a connection source to our database
        connectionSource = new JdbcConnectionSource(DATABASE_URL, "sa", "", new HsqldbDatabaseType());
        // instantiate the DAO to handle Account with String id
        vaultDao = DaoManager.createDao(connectionSource, VaultEntry.class);
        // if you need to create the 'accounts' table make this call
        TableUtils.createTableIfNotExists(connectionSource, VaultEntry.class);
    }

    public boolean putEntry(VaultEntry entry) {
        try {
            vaultDao.createIfNotExists(entry);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error saving entry:\n" + entry.toString(), ex);
            return false;
        }
        return true;
    }

    public boolean removeDublicates() throws SQLException {
        // DELETE FROM MyTable WHERE RowId NOT IN (SELECT MIN(RowId) FROM MyTable GROUP BY Col1, Col2, Col3);
        // but we need a workaround for the or mapper
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                            .prepare();
            CloseableIterator<VaultEntry> iterator = vaultDao.iterator(query);

            Date startGenerationTimestamp = null;
            List<VaultEntry> tmpList = new ArrayList<>();
            List<Long> dublicateId = new ArrayList<>();
            while (iterator.hasNext()) {
                VaultEntry entry = iterator.next();
                if (startGenerationTimestamp == null) {
                    // start up
                    startGenerationTimestamp = entry.getTimestamp();
                    tmpList.add(entry);
                } else if (!startGenerationTimestamp
                        .equals(entry.getTimestamp())) {
                    // not same timestamp --> new line generation
                    startGenerationTimestamp = entry.getTimestamp();
                    tmpList.clear();
                    tmpList.add(entry);
                } else {
                    // same timestamp --> check if it is a dublicate
                    for (VaultEntry item : tmpList){
                        if (item.equals(entry)){
                            // dublicate --> delete and move on
                            dublicateId.add(entry.getId());
                            break;
                        }
                    }
                }
            }
            
            // delete dublicates
            int lines = vaultDao.deleteIds(dublicateId);
            LOG.log(Level.INFO, "Removed {0} dublicates", lines);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }

        return true;
    }

    public List<VaultEntry> queryGlucoseBetween(Date from, Date to) {
        List<VaultEntry> returnValues = null;
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                            .where()
                            .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_BG)
                            .or()
                            .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_CGM)
                            .or()
                            .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_CGM_ALERT)
                            .between(VaultEntry.TIMESTAMP_FIELD_NAME, from, to)
                            .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public List<VaultEntry> queryAllVaultEntrys() {
        List<VaultEntry> returnValues = new ArrayList<>();
        try {

            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                            .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public List<VaultEntry> queryVaultEntrysBetween(Date from, Date to) {
        List<VaultEntry> returnValues = new ArrayList<>();
        try {
            Date fromTimestamp = TimestampUtils.createCleanTimestamp(from);
            Date toTimestamp = TimestampUtils.createCleanTimestamp(to);

            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                            .where()
                            .between(VaultEntry.TIMESTAMP_FIELD_NAME, fromTimestamp, toTimestamp)
                            .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }
}
