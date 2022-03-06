package service;

import dao.DataAccessException;
import result.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import dao.Database;

public class ClearService {
    private static Logger logger = Logger.getLogger("ClearService");
    Database db;
    Result result;
    /**
     * Clears all the tables in the database
     * @return
     */
    public Result processRequest() {
        db = new Database();
        result = new Result();
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            result.setMessage("Clear succeeded.");
            result.setSuccess(true);
        } catch (Exception ex){
            ex.printStackTrace();
            logger.info("exception thrown");
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
            logger.info(result.toString());
        } finally {
            try {
                Connection conn = db.getConnection();
                if (!conn.isClosed()) {
                    db.closeConnection(false);
                }
            } catch(SQLException ex){
                logger.info(ex.getMessage());
            } catch(DataAccessException ex){
                logger.info(ex.getMessage());
            }
        }
      return result;
    }
}
