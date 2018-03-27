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
package at.fhooe.mtd.sgl.graphics;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import at.fhooe.mtd.sgl.Sgl;
import at.fhooe.mtd.sgl.app.Application.LoopMode;
import at.fhooe.mtd.sgl.graphics.Graphics.Quality;


public class GfxConfigurator {
    
    private static FormatTag[] TAGS = { new FormatTag(16.0 / 9.0, "16:9"),
        new FormatTag(16.0 / 10.0, "16:10"),
        new FormatTag(4.0 / 3.0, "4:3") };
    
    private JFrame frame;
    private String title = "Graphics Configurator";
    private JComboBox<DisplayModeWrapper> modeCombo;
    private JComboBox<Quality> qualityCombo;
    private JComboBox<LoopModeWrapper> loopCombo;
    private JComboBox<GraphicsDeviceWrapper> deviceCombo;
    private JCheckBox fullScreenChk;
    private JCheckBox vsyncChk;

    private boolean finished;
    private String prefNodeName;

	private int forceWidth = -1;
	private int forceHeight = -1;
    
    private static String findTag(int width, int height) {
        for (FormatTag tag : TAGS) {
            if (tag.matches(width, height)) {
                return tag.getTag();
            }
        }
        return null;
    }
    
    private static class GraphicsDeviceWrapper {
    	private int idx;
    	private String name;
    	private DisplayModeWrapper[] modes;
    	
    	public GraphicsDeviceWrapper(String name, int idx, DisplayModeWrapper[] modes) {
    		this.idx = idx;
    		this.name = name;
    		this.modes = modes;
    	}
    	
        @Override
        public String toString() {
        	if (name != null) {
        		return name;
        	} else {
        		return String.format("Device %d", idx);
        	}
        }
    	
    }
    
    private static class LoopModeWrapper {
        private LoopMode mode;
        
        public LoopModeWrapper(LoopMode mode) {
            this.mode = mode;
        }
        
        public LoopMode getMode() {
            return mode;
        }

        @Override
        public String toString() {
            String s = mode.toString();
            return Character.toUpperCase(s.charAt(0))
                    + s.substring(1).toLowerCase().replace('_', ' ');
        }
    }
    
    private static class FormatTag {
        private static final double EPSILON = 0.001;
        
        double ar;
        String tag;
        
        public FormatTag(double ar, String tag) {
            this.ar = ar;
            this.tag = tag;
        }
        
        public boolean matches(int width, int height) {
            return Math.abs(ar - (double) width / height) < EPSILON;
        }
        
        public String getTag() {
            return tag;
        }
    }
        
    private static class DisplayModeWrapper implements
            Comparable<DisplayModeWrapper> {
        
        static StringBuilder builder = new StringBuilder();
        DisplayMode mode;
        
        DisplayModeWrapper(DisplayMode m) {
            mode = m;
        }

        @Override
        public String toString() {
            builder.setLength(0);
            builder
                .append(mode.getWidth())
                .append(" x ")
                .append(mode.getHeight());
            if (mode.getRefreshRate() != 60 && mode.getRefreshRate() > 0) {
                builder.append(' ')
                    .append('@')
                    .append(mode.getRefreshRate())
                    .append("Hz");
            }
            String tag = findTag(mode.getWidth(), mode.getHeight());
            if (tag != null) {
                builder.append(" (").append(tag).append(")");
            }
            return builder.toString();
        }

        @Override
        public int compareTo(DisplayModeWrapper o) {
            int result = Integer.compare(mode.getWidth(), o.mode.getWidth());
            if (result != 0) return result;
            
            result = Integer.compare(mode.getHeight(), o.mode.getHeight());
            if (result != 0) return result;
            
            result = Integer.compare(mode.getRefreshRate(),
                    o.mode.getRefreshRate());
            
            return result;
        }
    }
    
    
    public GfxConfigurator() {
        // intentionally left empty
    }
    
    public void setPreferencesNode(String nodeName) {
        prefNodeName = nodeName;
    }
    
    public String getPreferencesNode() {
        return prefNodeName;
    }
    
    private DisplayModeWrapper[] getModes() {
    	return getModes(-1);
    }
    
    private DisplayModeWrapper[] getModes(int idx) {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();

        GraphicsDevice gd;
        if (idx == -1) {
            gd = ge.getDefaultScreenDevice();        	
        } else {
        	gd = ge.getScreenDevices()[idx];
        }

        List<DisplayModeWrapper> r = new ArrayList<>();
        for (DisplayMode mode : gd.getDisplayModes()) {
            r.add(new DisplayModeWrapper(mode));
        }
        if (forceHeight > 0 && forceWidth > 0) {
        	DisplayMode dmSrc = r.get(0).mode;
			r.add(new DisplayModeWrapper(
					new DisplayMode(forceWidth, forceHeight, dmSrc.getBitDepth(), dmSrc.getRefreshRate())));
        }
        Collections.sort(r);
        Collections.reverse(r);

        return r.toArray(new DisplayModeWrapper[0]);
    }
    
 
    public void runDialog() {
        try {
            setFinished(false);
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					createAndShowGui();
				}
			});
            waitForFinished();
        } catch (InvocationTargetException | InterruptedException e) {
            throw new RuntimeException("unable to init gfx configurator", e);
        }
    }
    
    
    public DisplayMode getDisplayMode() {
        return modeCombo.getItemAt(modeCombo.getSelectedIndex()).mode;
    }
    
    public Quality getGraphicsQuality() {
        return qualityCombo.getItemAt(qualityCombo.getSelectedIndex());        
    }
    
    public int getGraphicsDevice() {
    	return deviceCombo.getItemAt(deviceCombo.getSelectedIndex()).idx;
    }

    public LoopMode getLoopMode() {
        return loopCombo.getItemAt(loopCombo.getSelectedIndex()).getMode();        
    }
    
    public boolean isFullScreen() {
        return fullScreenChk.isSelected();
    }
    
    public boolean isVSync() {
        return vsyncChk.isSelected();
    }
    
    private synchronized void setFinished(boolean value) {
        finished = value;
        notifyAll();        
    }
    
    private synchronized boolean isFinished() {
        return finished;
    }
    
    private synchronized void waitForFinished() {
        while (!isFinished()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void createAndShowGui() {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(createGui());
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        loadPreferences();
    }

    private Preferences getPreferences() {
        if (prefNodeName == null) {
            return Preferences.userNodeForPackage(Sgl.class);
        } else {
            return Preferences.userRoot().node(prefNodeName);
        }
    }
    
    private void loadPreferences() {
        Preferences prefs = getPreferences();
        vsyncChk.setSelected(prefs.getBoolean("vsync", true));
        fullScreenChk.setSelected(prefs.getBoolean("fullscreen", false));
        loopCombo.setSelectedIndex(prefs.getInt("loopmode", 0));
        qualityCombo.setSelectedIndex(prefs.getInt("quality", 1));
                
        DisplayMode m = new DisplayMode(prefs.getInt("width", 0), prefs.getInt(
                "height", 0), prefs.getInt("depth", 0), prefs.getInt("rate", 0));
        
        for (int i = 0; i < modeCombo.getItemCount(); ++i) {
            if (modeCombo.getItemAt(i).mode.equals(m)) {
                modeCombo.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void storePreferences() {
        Preferences prefs = getPreferences();
        prefs.putBoolean("vsync", vsyncChk.isSelected());
        prefs.putBoolean("fullscreen", fullScreenChk.isSelected());
        prefs.putInt("loopmode", loopCombo.getSelectedIndex());
        prefs.putInt("quality", qualityCombo.getSelectedIndex());
        DisplayMode m = modeCombo.getItemAt(modeCombo.getSelectedIndex()).mode;
        prefs.putInt("width", m.getWidth());
        prefs.putInt("height", m.getHeight());
        prefs.putInt("rate", m.getRefreshRate());
        prefs.putInt("depth", m.getBitDepth());
    }

    private Component createGui() {
        JPanel pane = new JPanel();
        modeCombo = new JComboBox<>(getModes());
        qualityCombo = new JComboBox<>(Quality.values());
        loopCombo = new JComboBox<>(getGameLoopModes());
        deviceCombo = new JComboBox<>(getGraphicsDeviceList());
        JLabel resLabel = new JLabel("Screen resolution");
        JLabel qualLabel = new JLabel("Graphics quality");
        JLabel loopLabel = new JLabel("Loop mode");
        JLabel deviceLabel = new JLabel("Screen");
        JButton okButton = new JButton("Ok");
        fullScreenChk = new JCheckBox("Full screen");
        vsyncChk = new JCheckBox("V-Sync");
        qualityCombo.setSelectedItem(Quality.Good);
        
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(resLabel)
                    .addComponent(qualLabel)
                    .addComponent(deviceLabel)
                    .addComponent(loopLabel))
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(modeCombo)
                    .addComponent(qualityCombo)
                    .addComponent(deviceCombo)
                    .addComponent(loopCombo)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(fullScreenChk)
                            .addComponent(vsyncChk)))
                .addComponent(okButton)
                );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(resLabel)
                        .addComponent(modeCombo))
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(qualLabel)
                        .addComponent(qualityCombo))
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(deviceLabel)
                        .addComponent(deviceCombo))
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(loopLabel)
                        .addComponent(loopCombo))
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(fullScreenChk)
                        .addComponent(vsyncChk))
                .addComponent(okButton)
                );
        
        if (numGraphicsDevices() <= 1) {
	        deviceCombo.setVisible(false);
	        deviceLabel.setVisible(false);
        }
        
        deviceCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicsDeviceWrapper item = (GraphicsDeviceWrapper) deviceCombo.getSelectedItem();
				modeCombo.removeAllItems();
				for (DisplayModeWrapper m : item.modes) {
					modeCombo.addItem(m);
				}
			}
		});
        
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit(true);
			}
		});
        
        return pane;
    }
    
    private int numGraphicsDevices() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return ge.getScreenDevices().length;
    }
    
    private GraphicsDeviceWrapper[] getGraphicsDeviceList() {
    	List<GraphicsDeviceWrapper> result = new ArrayList<>();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		GraphicsDevice[] devices = ge.getScreenDevices();
		for (int i = 0; i < devices.length; ++i) {
			GraphicsDevice dev = devices[i];
			result.add(new GraphicsDeviceWrapper(dev.getIDstring(), i, getModes(i)));
		}
		return result.toArray(new GraphicsDeviceWrapper[0]);
	}

	private LoopModeWrapper[] getGameLoopModes() {
        List<LoopModeWrapper> result = new ArrayList<>();
        for (LoopMode m : LoopMode.values()) {
            result.add(new LoopModeWrapper(m));
        }
        return result.toArray(new LoopModeWrapper[0]);
    }

    private void exit(boolean storePrefs) {
        setFinished(true);
        frame.dispose();
        storePreferences();        
    }
    
    public GfxConfigurator title(String title) {
        setTitle(title);
        return this;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public void forceResolution(int width, int height) {
		forceWidth = width;
		forceHeight = height;
	}
}