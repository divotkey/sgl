# Version History

## Version 1.5.0
Date: *2016-09-??*

- Added matrix and vector class for 3d environments.
- Add method `getFontRenderContext` to graphics interface.
- Improved vector classes, added Vector4d class.
- Additional functionality for mouse input.
- Add `setTitle` method to `Graphics` interface.
- Added option to force graphics mode in `GfxConfigurator'.
- Added resource loader from POWIDL (thanks a lot to Quantum Reboot).

## Version 1.4.1
Date: *2016-06-06*

- Moved library information into META-INF folder to avoid name clashes when repacked into other jar archives.


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