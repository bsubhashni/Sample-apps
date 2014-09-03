package main.java.com.couchbase;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;


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

    try {
      JsonDocument doc = bucket.get("sample1").toBlocking().single();
      System.out.println(doc);
    } catch (Exception ex){
      System.out.println(ex);
    }


    docObs.subscribe(getObs);
    try {
      Thread.sleep(1);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    System.out.println("Ops count " + opsColl.getCount() + "\nErrors count " + opsColl.getErrors());
    ViewQuery queryTemplate = ViewQuery.from("test","test");

    Observable<ViewRow> viewRowObservable =  bucket.query(queryTemplate).flatMap(
            new Func1<ViewResult, Observable<ViewRow>>() {
              public  Observable<ViewRow> call(ViewResult res) {
                return res.rows();
            }
    });

    Observer<ViewRow> viewRowObserver = new Observer<ViewRow>() {
      @Override
      public void onCompleted() {
        System.out.println("View rows completed");
      }

      @Override
      public void onError(Throwable throwable) {
          System.out.println(throwable.toString());
      }

      @Override
      public void onNext(ViewRow viewRow) {
        System.out.println(viewRow.id());
      }
    };

    viewRowObservable.subscribe(viewRowObserver);

  }
}
