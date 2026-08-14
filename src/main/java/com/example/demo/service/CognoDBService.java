package com.example.demo.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

@Service
public class CognoDBService {

    private final Driver driver;

    public CognoDBService(Driver driver) {
        this.driver = driver;
    }

    public String testConnection() {

        try (Session session = driver.session()) {

            return session
                    .run("RETURN 'CognoDB connection successful!' AS message")
                    .single()
                    .get("message")
                    .asString();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to connect to CognoDB",
                    ex
            );
        }
    }

    public boolean isDatabaseConnected() {

        try (Session session = driver.session()) {

            session.run("RETURN 1").single();

            return true;

        } catch (Exception ex) {

            return false;
        }
    }
}