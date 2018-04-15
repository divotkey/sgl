# Version History

## Version 1.6.1
Date: 2018-04-??

- Rewrite of sound engine core.
- Sound clips can now be pitched.
- Sound clips are faded out when stopped to avoid audio clicks during playback.
- Sound system can fade-out sound clips.
- Java2dApplicationConfig contains entry for size of audio buffer.
- Updated copyright information. Fixed issue in copyright string.
- Updated 'Hello, World!' test application.

## Version 1.6.0
Date: 2018-03-27

- Class `GameState` has generic type parameter specifying the concrete game class.
- Method `ResourceLoader.getStream()` throws FileNotFoundException instead of IllegalArgumentExcepiton.
- The `Game` class postpones state changes when triggered while updating until the update cycle is finished.
- SGL now supports simple audio playback. Have a look at the new audio package.
- SGL graphics configuration now supports multiple screen environments.
- Added 3x3 matrix utility class.

## Version 1.5.2
Date: *2017-06-24*

- Added support for application icons.
- Added matrix and vector class for 3d environments.
- Add method `getFontRenderContext` to graphics interface.
- Improved vector classes, added Vector4d class.
- Additional functionality for mouse input.
- Add `setTitle` method to `Graphics` interface.
- Added option to force graphics mode in `GfxConfigurator'.
- Added resource loader from POWIDL (thanks a lot to Quantum Reboot).
- Added hasInputListener method to input interface.
- Vertical synchronization can be activated and deactivated during runtime.

## Version 1.4.1
Date: *2016-06-06*

- Moved library information into META-INF folder to avoid name clashes when re-packed into other jar archives.

## Version 1.4.0
Date: *2016-05-09*

- Added support for transfer handlers (e.g. used for drag and drop operations).
- Added method `getMousePosition` to the `Graphics` interface, which provides more up-to-date 
information about the position of the mouse pointer.

## Version 1.3.0
Date: *2016-05-04*

- Added utility method `determineCommandKey` to get the command key for the current operating system.
- Improved input system. The `keyDown` method provides in addition to the key code the corresponding character.
- Added utility class to test if a character is printable.
- Added more JavaDoc comments.
- Added constructor to `Game` class taking an initial game state as parameter.
- Fixed bug in Java2dApplication causing the `resize` method not to be called on startup.
- Fixed bug causing the application to stop working when toggling full screen mode.
- Add constants to math utilities used to convert nanoseconds.
- Added `getAspectRatio` method to `Graphics` interface.
- Added some additional JavaDoc comments.
- Improved Game class, `switchState` has now a parameter if the state's  `resize` method should be called.
- Fixed bug in input system causing an IndexOutOfBoundsException.

## Version 1.2.0
Date: *2016-04-18*

- Added more JavaDoc comments.
- Added Vector2d class.

## Version 1.1.0
Date: *2016-04-05*

- Added support to retrieve font metrics outside the rendering cycle.
- Added *read-me* file.

## Version 1.0.0
Date: *2016-03-30*

- Initial Version