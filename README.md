Video Player for local files
===========================================================

This project is an example on how to build a simple video player Android application 
that can play videos stored in the external storage of the device.

Introduction
-------------

### Functionalities
* List all playable video found in the external storage.
* Play the video when the format is compatible with Android.
* Have buttons to switch to the next or previous video.
* Have volume controls inside the app.
* Have buttons to pause and resume the execution.
* Allow the user to select multiple videos at once and create a playlist.
* All videos in the playlist should play one after the other and the whole playlist should
loop.
* The user is able to give a name to the playlist and to store multiple playlists.
* The user can delete existing playlists.

### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Retrofit][retrofit] for REST api communication
* [Navigation][navigation] for implementing common, but complex navigation requirements
* [RxJava] [rxjava] for composing asynchronous and event-based streams by using observable sequences
* [RxCursorLoader] [rxcursorloader] for creating streams from CursorLoader 

[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[retrofit]: http://square.github.io/retrofit
[navigation]: https://developer.android.com/topic/libraries/architecture/navigation/
[rxjava]: https://github.com/ReactiveX/RxJava
[rxcursorloader]: https://github.com/Doctoror/RxCursorLoader

Pre-requisites
--------------

* Android Studio 3.2 Beta 5 or later and you know how to use it.

* Make sure Android Studio is updated, as well as your SDK and Gradle.
Otherwise, you may have to wait for a while until all the updates are done.

* A device or emulator that runs API level 27

You need to be solidly familiar with the Kotlin programming language,
object-oriented design concepts, and Android Development Fundamentals.
In particular:

* Basic layouts and widgets
* Some familiarity with fragment transitions
* DataBinding
* Fragment Lifecycle
* ViewModel
* Navigation Library
* Dependency Injection
* Observer design pattern
* Singleton design pattern

Getting Started
---------------

1. [Install Android Studio](https://developer.android.com/studio/install.html),
if you don't already have it. Make sure you use a version that supports Navigation library.
2. Download the example.
3. Import the example into Android Studio.
4. Build and run the example.

License
--------

Copyright 2018 Marian Vasilca.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
