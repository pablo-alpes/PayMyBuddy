/**
 * Pay my buddy application
 * V.0.1
 * @author: Pablo Miranda
 *
 * The customer journey developed is: Landing page -> Login -> Transaction page
 * Main architecture: MVC with a repository layer.
 * Query simplification: Spring Data JPA for basic CRUDs.
 *
 */

package com.paymybudy;

import com.paymybudy.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;

@SpringBootApplication
public class OcPayMyBuddyApplication {
    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) throws SQLException {

        SpringApplication.run(OcPayMyBuddyApplication.class, args);

        //Runs the sql script to initialize mock values in the DB
        //ScriptUtils.executeSqlScript(conn, new ClassPathResource(Constants.INITIALIZER));
    }

}
