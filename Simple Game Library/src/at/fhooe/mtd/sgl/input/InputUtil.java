/*******************************************************************************
 * Copyright (c) 2016 Roman Divotkey, Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *  
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *  
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/
package at.fhooe.mtd.sgl.input;

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/**
 * A collection of input related utility methods.
 */
public class InputUtil {
    
    /**
     * Returns if the specified character is a printable character.
     * 
     * @param c
     *            the character to test
     * @return {@code true} if the character is printable
     */
    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) && c != KeyEvent.CHAR_UNDEFINED
                && block != null && block != Character.UnicodeBlock.SPECIALS;
    }
    
    /**
     * Determines the system specific command key. On Windows and Unix systems
     * this will be the {@code VK_CONTROL} and {@code VK_META} on MacOS.
     * 
     * @return the key code of the command key
     */
    public static int determineCommandKey() {
        switch (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
        case Event.META_MASK:
            return KeyEvent.VK_META;

        case Event.CTRL_MASK:
            return KeyEvent.VK_CONTROL;

        case Event.ALT_MASK:
            return KeyEvent.VK_ALT;

        default:
            return KeyEvent.VK_CONTROL;
        }
    }    
}
