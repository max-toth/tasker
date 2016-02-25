package com.tasker.core.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.tasker.core.LocalStorage;
import com.tasker.core.Task;
import org.bson.Document;

/**
 * @author mtolstyh
 * @since 24.02.2016.
 */
public class DatabaseManager {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private ObjectMapper objectMapper;

    public DatabaseManager() {
        try {
            this.mongoClient = new MongoClient("localhost", 27017);
            this.db = mongoClient.getDatabase("tasker-db");
            this.objectMapper = new ObjectMapper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sync() {
        try {
            int inserted = 0,
                    updated = 0;
            MongoCollection<Document> taskCollection = db.getCollection("task");
            for (Task t: LocalStorage.getTaskList()) {
                Document document = Document.parse(objectMapper.writeValueAsString(t));
                Document fromDb = taskCollection.find(Filters.eq("number", t.getNumber())).limit(1).first();
                if (fromDb == null) {
                    taskCollection.insertOne(document);
                    inserted++;
                } else if (!fromDb.getBoolean("status").equals(document.getBoolean("status"))) {
                    fromDb.put("status", t.getStatus());
                    taskCollection.updateOne(Filters.eq("number", t.getNumber()), new Document("$set", new Document("status", t.getStatus())));
                    updated++;
                }
            }
            System.out.println("<---- " + inserted + " tasks were inserted into DB");
            System.out.println("<---- " + updated + " tasks were updated during merge");
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
