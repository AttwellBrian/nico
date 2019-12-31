package com.chabomakers.nico;

import com.chabomakers.nico.controllers.AuctionActionController;
import com.chabomakers.nico.controllers.CreateUserController;
import com.chabomakers.nico.controllers.GameStateController;
import com.chabomakers.nico.controllers.ResetController;
import com.chabomakers.nico.controllers.StartGameController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.util.ServiceLoader;
import javax.inject.Singleton;

@Module
abstract class ServerModule {

  @Provides
  @Singleton
  static Gson gson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
      gsonBuilder.registerTypeAdapterFactory(factory);
    }
    return gsonBuilder.create();
  }

  @Binds
  @IntoSet
  abstract Controller startGameController(StartGameController controller);

  @Binds
  @IntoSet
  abstract Controller usersController(GameStateController controller);

  @Binds
  @IntoSet
  abstract Controller resetController(ResetController controller);

  @Binds
  @IntoSet
  abstract Controller createUserController(CreateUserController controller);

  @Binds
  @IntoSet
  abstract Controller auctionBidController(AuctionActionController controller);

  @Binds
  @IntoSet
  abstract Interceptor devCorsInterceptor(DevCorsInterceptor interceptor);

  @Binds
  @IntoSet
  abstract Interceptor loggerInterceptor(LoggerInterceptor loggerInterceptor);
}
