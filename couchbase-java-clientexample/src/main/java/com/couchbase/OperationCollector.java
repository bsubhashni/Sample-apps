package main.java.com.couchbase;

/**
 * Created by subhashni on 8/29/14.
 */
public class OperationCollector {
  private int count = 0;
  private int errors = 0;

  public void incrementOpCount() {
    count++;
  }
  public int getCount() {
    return count;
  }
  public void incrementError(){
    errors++;
  }
  public int getErrors() {
    return errors;
  }
}
