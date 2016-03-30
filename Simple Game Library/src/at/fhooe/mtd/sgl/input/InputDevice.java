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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InputDevice {

    protected List<InputListener> listeners = new ArrayList<>();
    private Map<InputListener, Integer> priorities = new HashMap<>();

	private Comparator<InputListener> comp = new Comparator<InputListener>() {

		@Override
		public int compare(InputListener o1, InputListener o2) {
			return Integer.compare(priorities.get(o1), priorities.get(o2));
		}
	};
	
	public void addInputListener(InputListener l, int priority) {
		if (listeners.contains(l)) {
			throw new IllegalArgumentException("input listener already added");
		}
        listeners.add(l);
        priorities.put(l, priority);
        Collections.sort(listeners, comp);
	}
    
    public void removeInputListener(InputListener l) {
    	priorities.remove(l);
        listeners.remove(l);
    }
    
    protected void fireKeyDown(int keycode) {
        for (InputListener l : listeners) {
            if (l.keyDown(keycode) )
                break;
        }
    }
    
    protected void fireKeyUp(int keycode) {
        for (InputListener l : listeners) {
            if (l.keyUp(keycode) )
                break;
        }
    }
    
    protected void fireMouseMove(int x, int y) {
        for (InputListener l : listeners) {
            if (l.mouseMove(x, y) )
                break;
        }
    }    
    
    protected void fireMouseDown(int x, int y, int button) {
        for (InputListener l : listeners) {
            if (l.mouseDown(x, y, button) )
                break;
        }
    }

    protected void fireMouseUp(int x, int y, int button) {
        for (InputListener l : listeners) {
            if (l.mouseUp(x, y, button) )
                break;
        }
    }
    
    protected void fireMouseWheel(double amount, int button) {
        for (InputListener l : listeners) {
            if (l.scrolled(amount, button))
                break;
        }
    }
	
}
