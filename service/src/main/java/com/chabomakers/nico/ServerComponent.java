package com.chabomakers.nico;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ServerModule.class})
interface ServerComponent {
  Server server();
}
