Backend Debugging
-------
If you start the program with `./gradlew :service:dockerBuildImage && docker-compose down && docker-compose up` then
you can attach a remote debugger to port 5005.

Frontend Debugging
-------
You can run `npm start` from the frontend module. File watcher will cause automatic hot reloads.
