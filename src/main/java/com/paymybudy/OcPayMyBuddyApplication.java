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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;

@SpringBootApplication
public class OcPayMyBuddyApplication {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(OcPayMyBuddyApplication.class, args);
    }

}
