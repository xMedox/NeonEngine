package net.medox.neonengine.core;

import static org.lwjgl.glfw.GLFW.*;

import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.Window;

public class Input{
	public static final int MOUSE				= 0;
	public static final int KEYBOARD 			= 1;
	
	public static final int NUM_KEYCODES 		= 349; //120
	public static final int NUM_MOUSEBUTTONS 	= 8;
	
	public static final int WHEEL_UP            = 1;
	public static final int WHEEL_DOWN          = -1;
	
	public static final int BUTTON_LEFT         = GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_RIGHT        = GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MIDDLE       = GLFW_MOUSE_BUTTON_MIDDLE;
//	public static final int BUTTON_1 	        = GLFW_MOUSE_BUTTON_1;
//	public static final int BUTTON_2 	        = GLFW_MOUSE_BUTTON_2;
//	public static final int BUTTON_3 	        = GLFW_MOUSE_BUTTON_3;
	public static final int BUTTON_4 	        = GLFW_MOUSE_BUTTON_4;
	public static final int BUTTON_5            = GLFW_MOUSE_BUTTON_5;
	public static final int BUTTON_6            = GLFW_MOUSE_BUTTON_6;
	public static final int BUTTON_7            = GLFW_MOUSE_BUTTON_7;
	public static final int BUTTON_8            = GLFW_MOUSE_BUTTON_8;
	
	public static final int KEY_UNKNOWN 		= GLFW_KEY_UNKNOWN;
	public static final int KEY_SPACE 			= GLFW_KEY_SPACE;
	public static final int KEY_APOSTROPHE 		= GLFW_KEY_APOSTROPHE;
	public static final int KEY_COMMA 			= GLFW_KEY_COMMA;
	public static final int KEY_MINUS 			= GLFW_KEY_MINUS;
	public static final int KEY_PERIOD 			= GLFW_KEY_PERIOD;
	public static final int KEY_SLASH 			= GLFW_KEY_SLASH;
	public static final int KEY_0 				= GLFW_KEY_0;
	public static final int KEY_1 				= GLFW_KEY_1;
	public static final int KEY_2 				= GLFW_KEY_2;
	public static final int KEY_3 				= GLFW_KEY_3;
	public static final int KEY_4 				= GLFW_KEY_4;
	public static final int KEY_5 				= GLFW_KEY_5;
	public static final int KEY_6 				= GLFW_KEY_6;
	public static final int KEY_7 				= GLFW_KEY_7;
	public static final int KEY_8			 	= GLFW_KEY_8;
	public static final int KEY_9 				= GLFW_KEY_9;
	public static final int KEY_SEMICOLON 		= GLFW_KEY_SEMICOLON;
	public static final int KEY_EQUAL 			= GLFW_KEY_EQUAL;
	public static final int KEY_A 				= GLFW_KEY_A;
	public static final int KEY_B 				= GLFW_KEY_B;
	public static final int KEY_C			 	= GLFW_KEY_C;
	public static final int KEY_D 				= GLFW_KEY_D;
	public static final int KEY_E 				= GLFW_KEY_E;
	public static final int KEY_F 				= GLFW_KEY_F;
	public static final int KEY_G 				= GLFW_KEY_G;
	public static final int KEY_H 				= GLFW_KEY_H;
	public static final int KEY_I 				= GLFW_KEY_I;
	public static final int KEY_J 				= GLFW_KEY_J;
	public static final int KEY_K 				= GLFW_KEY_K;
	public static final int KEY_L 				= GLFW_KEY_L;
	public static final int KEY_M 				= GLFW_KEY_M;
	public static final int KEY_N 				= GLFW_KEY_N;
	public static final int KEY_O 				= GLFW_KEY_O;
	public static final int KEY_P 				= GLFW_KEY_P;
	public static final int KEY_Q 				= GLFW_KEY_Q;
	public static final int KEY_R 				= GLFW_KEY_R;
	public static final int KEY_S 				= GLFW_KEY_S;
	public static final int KEY_T 				= GLFW_KEY_T;
	public static final int KEY_U 				= GLFW_KEY_U;
	public static final int KEY_V 				= GLFW_KEY_V;
	public static final int KEY_W 				= GLFW_KEY_W;
	public static final int KEY_X 				= GLFW_KEY_X;
	public static final int KEY_Y 				= GLFW_KEY_Y;
	public static final int KEY_Z 				= GLFW_KEY_Z;
	public static final int KEY_LEFT_BRACKET 	= GLFW_KEY_LEFT_BRACKET;
	public static final int KEY_BACKSLASH 		= GLFW_KEY_BACKSLASH;
	public static final int KEY_RIGHT_BRACKET 	= GLFW_KEY_RIGHT_BRACKET;
	public static final int KEY_GRAVE_ACCENT 	= GLFW_KEY_GRAVE_ACCENT;
	public static final int KEY_WORLD_1 		= GLFW_KEY_WORLD_1;
	public static final int KEY_WORLD_2 		= GLFW_KEY_WORLD_2;
	public static final int KEY_ESCAPE 			= GLFW_KEY_ESCAPE;
	public static final int KEY_ENTER 			= GLFW_KEY_ENTER;
	public static final int KEY_TAB 			= GLFW_KEY_TAB;
	public static final int KEY_BACKSPACE 		= GLFW_KEY_BACKSPACE;
	public static final int KEY_INSERT 			= GLFW_KEY_INSERT;
	public static final int KEY_DELETE 			= GLFW_KEY_DELETE;
	public static final int KEY_RIGHT 			= GLFW_KEY_RIGHT;
	public static final int KEY_LEFT 			= GLFW_KEY_LEFT;
	public static final int KEY_DOWN 			= GLFW_KEY_DOWN;
	public static final int KEY_UP 				= GLFW_KEY_UP;
	public static final int KEY_PAGE_UP 		= GLFW_KEY_PAGE_UP;
	public static final int KEY_PAGE_DOWN 		= GLFW_KEY_PAGE_DOWN;
	public static final int KEY_HOME 			= GLFW_KEY_HOME;
	public static final int KEY_END 			= GLFW_KEY_END;
	public static final int KEY_CAPS_LOCK 		= GLFW_KEY_CAPS_LOCK;
	public static final int KEY_SCROLL_LOCK 	= GLFW_KEY_SCROLL_LOCK;
	public static final int KEY_NUM_LOCK 		= GLFW_KEY_NUM_LOCK;
	public static final int KEY_PRINT_SCREEN 	= GLFW_KEY_PRINT_SCREEN;
	public static final int KEY_PAUSE 			= GLFW_KEY_PAUSE;
	public static final int KEY_F1 				= GLFW_KEY_F1;
	public static final int KEY_F2 				= GLFW_KEY_F2;
	public static final int KEY_F3 				= GLFW_KEY_F3;
	public static final int KEY_F4 				= GLFW_KEY_F4;
	public static final int KEY_F5 				= GLFW_KEY_F5;
	public static final int KEY_F6 				= GLFW_KEY_F6;
	public static final int KEY_F7 				= GLFW_KEY_F7;
	public static final int KEY_F8 				= GLFW_KEY_F8;
	public static final int KEY_F9 				= GLFW_KEY_F9;
	public static final int KEY_F10 			= GLFW_KEY_F10;
	public static final int KEY_F11 			= GLFW_KEY_F11;
	public static final int KEY_F12			 	= GLFW_KEY_F12;
	public static final int KEY_F13 			= GLFW_KEY_F13;
	public static final int KEY_F14 			= GLFW_KEY_F14;
	public static final int KEY_F15 			= GLFW_KEY_F15;
	public static final int KEY_F16 			= GLFW_KEY_F16;
	public static final int KEY_F17 			= GLFW_KEY_F17;
	public static final int KEY_F18 			= GLFW_KEY_F18;
	public static final int KEY_F19 			= GLFW_KEY_F19;
	public static final int KEY_F20 			= GLFW_KEY_F20;
	public static final int KEY_F21 			= GLFW_KEY_F21;
	public static final int KEY_F22 			= GLFW_KEY_F22;
	public static final int KEY_F23 			= GLFW_KEY_F23;
	public static final int KEY_F24 			= GLFW_KEY_F24;
	public static final int KEY_F25 			= GLFW_KEY_F25;
	public static final int KEY_KP_0 			= GLFW_KEY_KP_0;
	public static final int KEY_KP_1 			= GLFW_KEY_KP_1;
	public static final int KEY_KP_2 			= GLFW_KEY_KP_2;
	public static final int KEY_KP_3 			= GLFW_KEY_KP_3;
	public static final int KEY_KP_4 			= GLFW_KEY_KP_4;
	public static final int KEY_KP_5 			= GLFW_KEY_KP_5;
	public static final int KEY_KP_6 			= GLFW_KEY_KP_6;
	public static final int KEY_KP_7 			= GLFW_KEY_KP_7;
	public static final int KEY_KP_8 			= GLFW_KEY_KP_8;
	public static final int KEY_KP_9 			= GLFW_KEY_KP_9;
	public static final int KEY_KP_DECIMAL 		= GLFW_KEY_KP_DECIMAL;
	public static final int KEY_KP_DIVIDE 		= GLFW_KEY_KP_DIVIDE;
	public static final int KEY_KP_MULTIPLY 	= GLFW_KEY_KP_MULTIPLY;
	public static final int KEY_KP_SUBTRACT 	= GLFW_KEY_KP_SUBTRACT;
	public static final int KEY_KP_ADD 			= GLFW_KEY_KP_ADD;
	public static final int KEY_KP_ENTER 		= GLFW_KEY_KP_ENTER;
	public static final int KEY_KP_EQUAL 		= GLFW_KEY_KP_EQUAL;
	public static final int KEY_LEFT_SHIFT 		= GLFW_KEY_LEFT_SHIFT;
	public static final int KEY_LEFT_CONTROL 	= GLFW_KEY_LEFT_CONTROL;
	public static final int KEY_LEFT_ALT 		= GLFW_KEY_LEFT_ALT;
	public static final int KEY_LEFT_SUPER 		= GLFW_KEY_LEFT_SUPER;
	public static final int KEY_RIGHT_SHIFT 	= GLFW_KEY_RIGHT_SHIFT;
	public static final int KEY_RIGHT_CONTROL 	= GLFW_KEY_RIGHT_CONTROL;
	public static final int KEY_RIGHT_ALT 		= GLFW_KEY_RIGHT_ALT;
	public static final int KEY_RIGHT_SUPER 	= GLFW_KEY_RIGHT_SUPER;
	public static final int KEY_MENU 			= GLFW_KEY_MENU;
	
//	public static boolean textInputMode;
	
	private static boolean[] keys = new boolean[NUM_KEYCODES];
	private static boolean[] mouse = new boolean[NUM_MOUSEBUTTONS];
	
	private static boolean[] lastKeys = new boolean[NUM_KEYCODES];
	private static boolean[] lastMouse = new boolean[NUM_MOUSEBUTTONS];
	
//	@SuppressWarnings("unused")
//	private static int wheelX;
	private static int wheelY;
	
	private static int mouseX;
	private static int mouseY;
	
	private static String charList = "";
	
	public static void update(){
//		wheelX = 0;
		wheelY = 0;
		charList = "";
		
		for(int i = 0; i < NUM_KEYCODES; i++){
			lastKeys[i] = getKey(i);
		}

		for(int i = 0; i < NUM_MOUSEBUTTONS; i++){
			lastMouse[i] = getMouse(i);
		}
	}
	
	public static void scroll(/*double x, */double y){
//		wheelX = (int)x;
		wheelY = (int)y;
	}
	
	public static void mousePos(double x, double y){
		mouseX = (int)x;
//		mouseY = (int)y;
		mouseY = Window.getHeight()-1 - (int)y;
	}
	
	public static void mouseButton(int button, int action, int mods){
		if(button < NUM_MOUSEBUTTONS){
			mouse[button] = action != GLFW_RELEASE;
		}
	}
	
	public static void key(int key, int scancode, int action, int mods){
//		if(textInputMode == false){
			if(key != GLFW_KEY_UNKNOWN && key < NUM_KEYCODES){
				keys[key] = action != GLFW_RELEASE;
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
		
		int count = 0;
		
		for(int i = 0; i < NUM_KEYCODES; i++){
			if(getKey(i)){
				if(count == 0){
					result.setDevice(KEYBOARD);
					result.setKeycode(i);
				}
				
				count++;
			}
		}
		
		for(int i = 0; i < NUM_MOUSEBUTTONS; i++){
			if(getMouse(i)){
				if(count == 0){
					result.setDevice(MOUSE);
					result.setKeycode(i);
				}
				
				count++;
			}
		}
		
		return result;
	}
	
	public static boolean inputKey(InputKey key){
		return key.getDevice() == MOUSE ? getMouse(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKey(key.getKeycode()) : false;
		
//		if(key.getDevice() == MOUSE){
//			return getMouse(key.getKeycode());
//		}else if(key.getDevice() == KEYBOARD){
//			return getKey(key.getKeycode());
//		}
//		
//		return false;
	}
	
	public static boolean inputKeyDown(InputKey key){
		return key.getDevice() == MOUSE ? getMouseDown(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKeyDown(key.getKeycode()) : false;
		
//		if(key.getDevice() == MOUSE){
//			return getMouseDown(key.getKeycode());
//		}else if(key.getDevice() == KEYBOARD){
//			return getKeyDown(key.getKeycode());
//		}
//		
//		return false;
	}
	
	public static boolean inputKeyUp(InputKey key){
		return key.getDevice() == MOUSE ? getMouseUp(key.getKeycode()) : key.getDevice() == KEYBOARD ? getKeyUp(key.getKeycode()) : false;
		
//		if(key.getDevice() == MOUSE){
//			return getMouseUp(key.getKeycode());
//		}else if(key.getDevice() == KEYBOARD){
//			return getKeyUp(key.getKeycode());
//		}
//		
//		return false;
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
//		int dir = 0;
//		
//		if(wheelY < 0){
//			dir = -1;
//	    }
//		
//		if(wheelY > 0){
//	    	dir = 1;
//	    }
//				
//		return wheelDirection == dir;
		
		return wheelY < 0 ? wheelDirection == -1 : wheelY > 0 ? wheelDirection == 1 : wheelDirection == 0;
	}
	
	public static int getMouseWheelValue(){
		return wheelY;
	}
	
	public static Vector2f getMousePosition(){
//		ByteBuffer xPos = ByteBuffer.allocateDirect(8);
//		ByteBuffer yPos = ByteBuffer.allocateDirect(8);
//		glfwGetCursorPos(Window.window, xPos, yPos);
//		
//		xPos.order(ByteOrder.LITTLE_ENDIAN);
//		yPos.order(ByteOrder.LITTLE_ENDIAN);
//		
//		int x = (int)xPos.getDouble();
//		int y = (((int)yPos.getDouble()) - Window.getHeight()+1) * -1;
//		
//		return new Vector2f(x, y);
		
		return new Vector2f(mouseX, mouseY);
	}
	
	public static void setMousePosition(Vector2f pos){
		mouseX = (int)pos.getX();
		mouseY = (int)pos.getY();
		
//		double x = (double)mouseX;
//		double y = (double)(Window.getHeight()-1 - mouseY);
//		double y = (double)mouseY;
		
		glfwSetCursorPos(Window.window, (double)mouseX, (double)(Window.getHeight()-1 - mouseY));
	}
	
	public static void setGrabbed(boolean value){
		if(value){
			glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}else{
			glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
	
	public static boolean isGrabbed(){
		return glfwGetInputMode(Window.window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
	}
	
	public static String getChars(){
		return charList;
	}
}