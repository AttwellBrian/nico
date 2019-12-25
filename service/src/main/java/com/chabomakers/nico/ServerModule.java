package com.chabomakers.nico;

import com.chabomakers.nico.controllers.CreateUserController;
import com.chabomakers.nico.controllers.GameStateController;
import com.chabomakers.nico.controllers.StartGameController;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.util.ServiceLoader;
import java.util.Set;
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
  abstract Controller createUserController(CreateUserController controller);

  @Provides
  static Set<Interceptor> interceptorList() {
    // Empty list for now. Will add more later.
    return Sets.newHashSet();
  }
}
