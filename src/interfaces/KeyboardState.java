package interfaces;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import javax.swing.AbstractAction;

public class KeyboardState extends AbstractAction {

	/**
	 * Stores the state of the keyboard, if it's in the set, the key is down. If
	 * not, it's up.
	 */
	private static HashSet<String> state = new HashSet<>();

	private HashSet<String> localState;

	private int type;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8669624174872378806L;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (type == 1) {
			if (!state.contains(e.getActionCommand())) {
				synchronized (this) {
					state.add(e.getActionCommand());
				}
			}
		} else if (type == 2) {
			if (state.contains(e.getActionCommand())) {
				synchronized (this) {
					state.remove(e.getActionCommand());
				}
			}
		}
	}

	static KeyboardState getKeyPressListner() {
		KeyboardState kbs = new KeyboardState();
		kbs.type = 1;
		return kbs;
	}

	static KeyboardState getKeyReleaseListner() {
		KeyboardState kbs = new KeyboardState();
		kbs.type = 2;
		return kbs;
	}

	public static KeyboardState getKeyboardState() {
		KeyboardState kbs = new KeyboardState();
		kbs.localState = new HashSet<>();
		for (String s : state) {
			kbs.localState.add(s);
		}
		kbs.type = 3;
		return kbs;
	}

	public boolean isKeyUp(String key) {
		if (type != 3) {
			return !localState.contains(key);
		}
		return !localState.contains(key);
	}

	public boolean isKeyDown(String key) {
		if (type != 3) {
			return localState.contains(key);
		}
		return localState.contains(key);
	}
	
	public static boolean keyPressed(String key, KeyboardState old) {
		return (state.contains(key) && !old.localState.contains(key));
	}
	
	public static boolean keyReleased(String key, KeyboardState old) {
		return (!state.contains(key) && old.localState.contains(key));
	}
}
