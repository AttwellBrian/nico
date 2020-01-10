Backend Debugging
-------
Start the program with `./gradlew run` then you can attach a remote debugger to port 5005.

Frontend Debugging
-------
You can run `yarn start` from the frontend module. File watcher will cause automatic hot reloads.
You may need to perform a `yarn install` the first time to pull in dependencies. 

Release Pipeline
-------
Whenever you land a diff on master a release workflow is kicked off. This updates update https://nico-service-pwe5kqqlfa-uc.a.run.app/.