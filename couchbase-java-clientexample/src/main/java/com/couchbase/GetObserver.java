package main.java.com.couchbase;

import rx.Observer;

/**
 * Created by subhashni on 8/29/14.
 */
public class GetObserver<T> implements Observer {
  private int status = 0;
  private OperationCollector op = null;

  public GetObserver(OperationCollector op){
    this.op = op;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public void onCompleted() {
    status = 0;
    op.incrementOpCount();
  }

  @Override
  public void onNext(Object o) {
    status = 0;
  }

  @Override
  public void onError(Throwable throwable) {
    status = -1;
    op.incrementOpCount();
    op.incrementError();
  }
}
