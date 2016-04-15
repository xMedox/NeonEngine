package net.medox.neonengine.core;

import org.lwjgl.glfw.GLFW;

import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.Window;

public class Input{
	public static final int NUM_KEYCODES 		= 349;
	public static final int NUM_MOUSEBUTTONS 	= 8;
	
	private static final boolean[] keys = new boolean[NUM_KEYCODES];
	private static final boolean[] mouse = new boolean[NUM_MOUSEBUTTONS];
	
	private static final boolean[] lastKeys = new boolean[NUM_KEYCODES];
	private static final boolean[] lastMouse = new boolean[NUM_MOUSEBUTTONS];
	
	public static final int MOUSE				= 0;
	public static final int KEYBOARD 			= 1;
	
	public static final int BUTTON_LEFT         = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_RIGHT        = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MIDDLE       = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int BUTTON_1 	        = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int BUTTON_2 	        = GLFW.GLFW_MOUSE_BUTTON_2;
	public static final int BUTTON_3 	        = GLFW.GLFW_MOUSE_BUTTON_3;
	public static final int BUTTON_4 	        = GLFW.GLFW_MOUSE_BUTTON_4;
	public static final int BUTTON_5            = GLFW.GLFW_MOUSE_BUTTON_5;
	public static final int BUTTON_6            = GLFW.GLFW_MOUSE_BUTTON_6;
	public static final int BUTTON_7            = GLFW.GLFW_MOUSE_BUTTON_7;
	public static final int BUTTON_8            = GLFW.GLFW_MOUSE_BUTTON_8;
	
	public static final int WHEEL_UP            = 1;
	public static final int WHEEL_DOWN          = -1;
	
	public static final int KEY_UNKNOWN 		= GLFW.GLFW_KEY_UNKNOWN;
	public static final int KEY_SPACE 			= GLFW.GLFW_KEY_SPACE;
	public static final int KEY_APOSTROPHE 		= GLFW.GLFW_KEY_APOSTROPHE;
	public static final int KEY_COMMA 			= GLFW.GLFW_KEY_COMMA;
	public static final int KEY_MINUS 			= GLFW.GLFW_KEY_MINUS;
	public static final int KEY_PERIOD 			= GLFW.GLFW_KEY_PERIOD;
	public static final int KEY_SLASH 			= GLFW.GLFW_KEY_SLASH;
	public static final int KEY_0 				= GLFW.GLFW_KEY_0;
	public static final int KEY_1 				= GLFW.GLFW_KEY_1;
	public static final int KEY_2 				= GLFW.GLFW_KEY_2;
	public static final int KEY_3 				= GLFW.GLFW_KEY_3;
	public static final int KEY_4 				= GLFW.GLFW_KEY_4;
	public static final int KEY_5 				= GLFW.GLFW_KEY_5;
	public static final int KEY_6 				= GLFW.GLFW_KEY_6;
	public static final int KEY_7 				= GLFW.GLFW_KEY_7;
	public static final int KEY_8			 	= GLFW.GLFW_KEY_8;
	public static final int KEY_9 				= GLFW.GLFW_KEY_9;
	public static final int KEY_SEMICOLON 		= GLFW.GLFW_KEY_SEMICOLON;
	public static final int KEY_EQUAL 			= GLFW.GLFW_KEY_EQUAL;
	public static final int KEY_A 				= GLFW.GLFW_KEY_A;
	public static final int KEY_B 				= GLFW.GLFW_KEY_B;
	public static final int KEY_C			 	= GLFW.GLFW_KEY_C;
	public static final int KEY_D 				= GLFW.GLFW_KEY_D;
	public static final int KEY_E 				= GLFW.GLFW_KEY_E;
	public static final int KEY_F 				= GLFW.GLFW_KEY_F;
	public static final int KEY_G 				= GLFW.GLFW_KEY_G;
	public static final int KEY_H 				= GLFW.GLFW_KEY_H;
	public static final int KEY_I 				= GLFW.GLFW_KEY_I;
	public static final int KEY_J 				= GLFW.GLFW_KEY_J;
	public static final int KEY_K 				= GLFW.GLFW_KEY_K;
	public static final int KEY_L 				= GLFW.GLFW_KEY_L;
	public static final int KEY_M 				= GLFW.GLFW_KEY_M;
	public static final int KEY_N 				= GLFW.GLFW_KEY_N;
	public static final int KEY_O 				= GLFW.GLFW_KEY_O;
	public static final int KEY_P 				= GLFW.GLFW_KEY_P;
	public static final int KEY_Q 				= GLFW.GLFW_KEY_Q;
	public static final int KEY_R 				= GLFW.GLFW_KEY_R;
	public static final int KEY_S 				= GLFW.GLFW_KEY_S;
	public static final int KEY_T 				= GLFW.GLFW_KEY_T;
	public static final int KEY_U 				= GLFW.GLFW_KEY_U;
	public static final int KEY_V 				= GLFW.GLFW_KEY_V;
	public static final int KEY_W 				= GLFW.GLFW_KEY_W;
	public static final int KEY_X 				= GLFW.GLFW_KEY_X;
	public static final int KEY_Y 				= GLFW.GLFW_KEY_Y;
	public static final int KEY_Z 				= GLFW.GLFW_KEY_Z;
	public static final int KEY_LEFT_BRACKET 	= GLFW.GLFW_KEY_LEFT_BRACKET;
	public static final int KEY_BACKSLASH 		= GLFW.GLFW_KEY_BACKSLASH;
	public static final int KEY_RIGHT_BRACKET 	= GLFW.GLFW_KEY_RIGHT_BRACKET;
	public static final int KEY_GRAVE_ACCENT 	= GLFW.GLFW_KEY_GRAVE_ACCENT;
	public static final int KEY_WORLD_1 		= GLFW.GLFW_KEY_WORLD_1;
	public static final int KEY_WORLD_2 		= GLFW.GLFW_KEY_WORLD_2;
	public static final int KEY_ESCAPE 			= GLFW.GLFW_KEY_ESCAPE;
	public static final int KEY_ENTER 			= GLFW.GLFW_KEY_ENTER;
	public static final int KEY_TAB 			= GLFW.GLFW_KEY_TAB;
	public static final int KEY_BACKSPACE 		= GLFW.GLFW_KEY_BACKSPACE;
	public static final int KEY_INSERT 			= GLFW.GLFW_KEY_INSERT;
	public static final int KEY_DELETE 			= GLFW.GLFW_KEY_DELETE;
	public static final int KEY_RIGHT 			= GLFW.GLFW_KEY_RIGHT;
	public static final int KEY_LEFT 			= GLFW.GLFW_KEY_LEFT;
	public static final int KEY_DOWN 			= GLFW.GLFW_KEY_DOWN;
	public static final int KEY_UP 				= GLFW.GLFW_KEY_UP;
	public static final int KEY_PAGE_UP 		= GLFW.GLFW_KEY_PAGE_UP;
	public static final int KEY_PAGE_DOWN 		= GLFW.GLFW_KEY_PAGE_DOWN;
	public static final int KEY_HOME 			= GLFW.GLFW_KEY_HOME;
	public static final int KEY_END 			= GLFW.GLFW_KEY_END;
	public static final int KEY_CAPS_LOCK 		= GLFW.GLFW_KEY_CAPS_LOCK;
	public static final int KEY_SCROLL_LOCK 	= GLFW.GLFW_KEY_SCROLL_LOCK;
	public static final int KEY_NUM_LOCK 		= GLFW.GLFW_KEY_NUM_LOCK;
	public static final int KEY_PRINT_SCREEN 	= GLFW.GLFW_KEY_PRINT_SCREEN;
	public static final int KEY_PAUSE 			= GLFW.GLFW_KEY_PAUSE;
	public static final int KEY_F1 				= GLFW.GLFW_KEY_F1;
	public static final int KEY_F2 				= GLFW.GLFW_KEY_F2;
	public static final int KEY_F3 				= GLFW.GLFW_KEY_F3;
	public static final int KEY_F4 				= GLFW.GLFW_KEY_F4;
	public static final int KEY_F5 				= GLFW.GLFW_KEY_F5;
	public static final int KEY_F6 				= GLFW.GLFW_KEY_F6;
	public static final int KEY_F7 				= GLFW.GLFW_KEY_F7;
	public static final int KEY_F8 				= GLFW.GLFW_KEY_F8;
	public static final int KEY_F9 				= GLFW.GLFW_KEY_F9;
	public static final int KEY_F10 			= GLFW.GLFW_KEY_F10;
	public static final int KEY_F11 			= GLFW.GLFW_KEY_F11;
	public static final int KEY_F12			 	= GLFW.GLFW_KEY_F12;
	public static final int KEY_F13 			= GLFW.GLFW_KEY_F13;
	public static final int KEY_F14 			= GLFW.GLFW_KEY_F14;
	public static final int KEY_F15 			= GLFW.GLFW_KEY_F15;
	public static final int KEY_F16 			= GLFW.GLFW_KEY_F16;
	public static final int KEY_F17 			= GLFW.GLFW_KEY_F17;
	public static final int KEY_F18 			= GLFW.GLFW_KEY_F18;
	public static final int KEY_F19 			= GLFW.GLFW_KEY_F19;
	public static final int KEY_F20 			= GLFW.GLFW_KEY_F20;
	public static final int KEY_F21 			= GLFW.GLFW_KEY_F21;
	public static final int KEY_F22 			= GLFW.GLFW_KEY_F22;
	public static final int KEY_F23 			= GLFW.GLFW_KEY_F23;
	public static final int KEY_F24 			= GLFW.GLFW_KEY_F24;
	public static final int KEY_F25 			= GLFW.GLFW_KEY_F25;
	public static final int KEY_KP_0 			= GLFW.GLFW_KEY_KP_0;
	public static final int KEY_KP_1 			= GLFW.GLFW_KEY_KP_1;
	public static final int KEY_KP_2 			= GLFW.GLFW_KEY_KP_2;
	public static final int KEY_KP_3 			= GLFW.GLFW_KEY_KP_3;
	public static final int KEY_KP_4 			= GLFW.GLFW_KEY_KP_4;
	public static final int KEY_KP_5 			= GLFW.GLFW_KEY_KP_5;
	public static final int KEY_KP_6 			= GLFW.GLFW_KEY_KP_6;
	public static final int KEY_KP_7 			= GLFW.GLFW_KEY_KP_7;
	public static final int KEY_KP_8 			= GLFW.GLFW_KEY_KP_8;
	public static final int KEY_KP_9 			= GLFW.GLFW_KEY_KP_9;
	public static final int KEY_KP_DECIMAL 		= GLFW.GLFW_KEY_KP_DECIMAL;
	public static final int KEY_KP_DIVIDE 		= GLFW.GLFW_KEY_KP_DIVIDE;
	public static final int KEY_KP_MULTIPLY 	= GLFW.GLFW_KEY_KP_MULTIPLY;
	public static final int KEY_KP_SUBTRACT 	= GLFW.GLFW_KEY_KP_SUBTRACT;
	public static final int KEY_KP_ADD 			= GLFW.GLFW_KEY_KP_ADD;
	public static final int KEY_KP_ENTER 		= GLFW.GLFW_KEY_KP_ENTER;
	public static final int KEY_KP_EQUAL 		= GLFW.GLFW_KEY_KP_EQUAL;
	public static final int KEY_LEFT_SHIFT 		= GLFW.GLFW_KEY_LEFT_SHIFT;
	public static final int KEY_LEFT_CONTROL 	= GLFW.GLFW_KEY_LEFT_CONTROL;
	public static final int KEY_LEFT_ALT 		= GLFW.GLFW_KEY_LEFT_ALT;
	public static final int KEY_LEFT_SUPER 		= GLFW.GLFW_KEY_LEFT_SUPER;
	public static final int KEY_RIGHT_SHIFT 	= GLFW.GLFW_KEY_RIGHT_SHIFT;
	public static final int KEY_RIGHT_CONTROL 	= GLFW.GLFW_KEY_RIGHT_CONTROL;
	public static final int KEY_RIGHT_ALT 		= GLFW.GLFW_KEY_RIGHT_ALT;
	public static final int KEY_RIGHT_SUPER 	= GLFW.GLFW_KEY_RIGHT_SUPER;
	public static final int KEY_MENU 			= GLFW.GLFW_KEY_MENU;
	
//	public static boolean textInputMode;
	
	private static int wheelY;
	
	private static int mouseX;
	private static int mouseY;
	
	private static String charList = "";
	
	public static void update(){
		wheelY = 0;
		charList = "";
		
		for(int i = 0; i < NUM_KEYCODES; i++){
			lastKeys[i] = getKey(i);
		}

		for(int i = 0; i < NUM_MOUSEBUTTONS; i++){
			lastMouse[i] = getMouse(i);
		}
	}
	
	public static void scroll(double y){
		wheelY = (int)y;
	}
	
	public static void mousePos(double x, double y){
		mouseX = (int)x;
		mouseY = Window.getHeight()-1 - (int)y;
	}
	
	public static void mouseButton(int button, int action, int mods){
		if(button < NUM_MOUSEBUTTONS){
			mouse[button] = action != GLFW.GLFW_RELEASE;
		}
	}
	
	public static void key(int key, int scancode, int action, int mods){
//		if(textInputMode == false){
			if(key != GLFW.GLFW_KEY_UNKNOWN && key < NUM_KEYCODES){
				keys[key] = action != GLFW.GLFW_RELEASE;
			}
//		}
	}
	
	public static void chars(int codepoint){
//		if(textInputMode == true){
			charList += Character.toString((char)codepoint);
//		}
	}
	
//	public static void setTextInputMode(boolean value){
//		textInputMode = value;
//	}
//	
//	public static boolean getTextInputMode(){
//		return textInputMode;
//	}
	
	public static InputKey getCurrentKey(){
		final InputKey result = new InputKey(-1, -1);
		
		for(int i = 0; i < NUM_KEYCODES; i++){
			if(getKey(i)){
				result.setDevice(KEYBOARD);
				result.setKeycode(i);
				
				break;
			}
		}
		
		if(result.getDevice() == -1 && result.getKeycode() == -1){
			for(int i = 0; i < NUM_MOUSEBUTTONS; i++){
				if(getMouse(i)){
					result.setDevice(MOUSE);
					result.setKeycode(i);
					
					break;
				}
			}
		}
		
		return result;
	}
	
	public static boolean inputKey(InputKey key){
		return key.getDevice() == MOUSE ? getMouse(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKey(key.getKeycode()) : false;
	}
	
	public static boolean inputKeyDown(InputKey key){
		return key.getDevice() == MOUSE ? getMouseDown(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKeyDown(key.getKeycode()) : false;
	}
	
	public static boolean inputKeyUp(InputKey key){
		return key.getDevice() == MOUSE ? getMouseUp(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKeyUp(key.getKeycode()) : false;
	}
	
	public static boolean getKey(int keyCode){
		return keys[keyCode];
	}
	
	public static boolean getKeyDown(int keyCode){
		return getKey(keyCode) && !lastKeys[keyCode];
	}
	
	public static boolean getKeyUp(int keyCode){
		return !getKey(keyCode) && lastKeys[keyCode];
	}
	
	public static boolean getMouse(int mouseButton){
		return mouse[mouseButton];
	}
	
	public static boolean getMouseDown(int mouseButton){
		return getMouse(mouseButton) && !lastMouse[mouseButton];
	}
	
	public static boolean getMouseUp(int mouseButton){
		return !getMouse(mouseButton) && lastMouse[mouseButton];
	}
	
	public static boolean getMouseWheelDirection(int wheelDirection){
		return wheelY < 0 ? wheelDirection == -1 : wheelY > 0 ? wheelDirection == 1 : wheelDirection == 0;
	}
	
	public static int getMouseWheelValue(){
		return wheelY;
	}
	
	public static Vector2f getMousePosition(){
		return new Vector2f(mouseX, mouseY);
	}
	
	public static void setMousePosition(Vector2f pos){
		mouseX = (int)pos.getX();
		mouseY = (int)pos.getY();
		
		GLFW.glfwSetCursorPos(Window.window, (double)mouseX, (double)(Window.getHeight()-1 - mouseY));
	}
	
	public static void setGrabbed(boolean value){
		if(value){
			GLFW.glfwSetInputMode(Window.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}else{
			GLFW.glfwSetInputMode(Window.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}
	
	public static boolean isGrabbed(){
		return GLFW.glfwGetInputMode(Window.window, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
	}
	
	public static String getChars(){
		return charList;
	}
}
