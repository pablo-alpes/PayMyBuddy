package com.paymybudy;

import com.paymybudy.configuration.DatabaseConfig;
import com.paymybudy.constants.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class OcPayMyBuddyApplication {

    public static void main(String[] args) throws SQLException {

        SpringApplication.run(OcPayMyBuddyApplication.class, args);

        //DB CONNECTION
        DatabaseConfig databaseConfig = new DatabaseConfig();
        Connection conn = databaseConfig.getConnection();

        //Runs the sql script to initialize mock values in the DB
        ScriptUtils.executeSqlScript(conn, new ClassPathResource(Constants.INITIALIZER));
    }

}
