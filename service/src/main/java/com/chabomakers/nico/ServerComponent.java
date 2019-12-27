package com.chabomakers.nico;

import com.chabomakers.nico.gamestate.GameState;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ServerModule.class})
interface ServerComponent {
  Server server();

  GameState memoryDatabase();
}
