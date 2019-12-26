package com.chabomakers.nico;

import com.chabomakers.nico.database.MemoryDatabase;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ServerModule.class})
interface ServerComponent {
  Server server();

  MemoryDatabase memoryDatabase();
}
