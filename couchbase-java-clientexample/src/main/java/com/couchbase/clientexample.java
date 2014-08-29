package main.java.com.couchbase;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import rx.Observable;


/**
 * Created by subhashni on 8/28/14.
 */
public class clientexample {
  public static void main(String []args) {

    Cluster cluster = CouchbaseCluster.fromConnectionString("couchbase://127.0.0.1");
    Bucket bucket = cluster.openBucket("default", "").toBlocking().single();

    JsonObject user = JsonObject.empty();
    user.put("First Name", "Subhashni");

    bucket.upsert(JsonDocument.create("sample", user));
    Observable<JsonDocument> docObs = bucket.get("sample1");

    //System.out.println("Observable sequence" + docObs);

    OperationCollector opsColl = new OperationCollector();
    GetObserver<JsonDocument> getObs = new GetObserver<JsonDocument>(opsColl);

    docObs.subscribe(getObs);
    try {
      Thread.sleep(1);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    System.out.println("Ops count " + opsColl.getCount() + "\nErrors count " + opsColl.getErrors());
  }
}
