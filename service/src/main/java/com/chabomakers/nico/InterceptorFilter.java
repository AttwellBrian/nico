package com.chabomakers.nico;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import spark.Filter;
import spark.Request;
import spark.Response;

/** A {@link Filter} that registers {@link Interceptor Interceptors}. */
@Singleton
class InterceptorFilter implements Filter {

  private final Set<Interceptor> interceptors;
  private final Set<Request> requests = ConcurrentHashMap.newKeySet();

  @Inject
  InterceptorFilter(Set<Interceptor> interceptors) {
    this.interceptors = interceptors;
  }

  @Override
  public void handle(Request request, Response response) {
    if (!requests.contains(request)) {
      requests.add(request);
      interceptors.forEach(interceptor -> interceptor.before(request, response));
      return;
    }
    requests.remove(request);
    interceptors.forEach(interceptor -> interceptor.afterAfter(request, response));
  }
}
