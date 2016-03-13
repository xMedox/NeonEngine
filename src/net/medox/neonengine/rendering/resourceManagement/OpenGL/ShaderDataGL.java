package net.medox.neonengine.rendering.resourceManagement.OpenGL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.DataUtil;
import net.medox.neonengine.rendering.resourceManagement.ShaderData;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class ShaderDataGL extends ShaderData{
//	private static int bound;
	
	private final int program;
	private final List<Integer> shaders;
	private final List<String> uniformNames;
	private final List<String> uniformTypes;
	private final Map<String, Integer> uniformMap;
	
	private final String fileName;
	
	public ShaderDataGL(String fileName){
		super(fileName);
		
		this.fileName = fileName;
		
		this.program = GL20.glCreateProgram();
		
		if(program == 0){
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		
		shaders = new ArrayList<Integer>();
		uniformNames = new ArrayList<String>();
		uniformTypes = new ArrayList<String>();
		uniformMap = new HashMap<String, Integer>();
		
		final String vetexShaderText = loadShader(fileName + ".vs");
		final String fragmentShaderText = loadShader(fileName + ".fs");

		addVertexShader(vetexShaderText);
		addFragmentShader(fragmentShaderText);
		
		addAllAttributes(vetexShaderText);
		
		compileShader();
		
		addShaderUniforms(vetexShaderText);
		addShaderUniforms(fragmentShaderText);
	}
	
	private void addVertexShader(String text){
		addProgram(text, GL20.GL_VERTEX_SHADER);
	}
	
	@SuppressWarnings("unused")
	private void addGeometryShader(String text){
		addProgram(text, GL32.GL_GEOMETRY_SHADER);
	}
	
	private void addFragmentShader(String text){
		addProgram(text, GL20.GL_FRAGMENT_SHADER);
	}
	
	private void addProgram(String text, int type){
		final int shader = GL20.glCreateShader(type);
		
		if(shader == 0){
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);
		
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0){
			System.out.println(fileName);
			
			System.err.println(GL20.glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		GL20.glAttachShader(program, shader);
		shaders.add(shader);
	}
	
	
	private void addAllAttributes(String shaderText){
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int attribNumber = 0;
		
		while(attributeStartLocation != -1){
			if(!(attributeStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText.charAt(attributeStartLocation - 1) == ';')
					&& Character.isWhitespace(shaderText.charAt(attributeStartLocation + ATTRIBUTE_KEYWORD.length())))) {
				attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
				continue;
			}

			final int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
//			int end = shaderText.indexOf(";", begin);
			
			final String attributeLine = shaderText.substring(begin, shaderText.indexOf(";", begin)).trim();
//			final String attributeLine = shaderText.substring(begin, end).trim();
//			String attributeName = attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim();

//			setAttribLocation(attributeName, attribNumber);
			setAttribLocation(attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim(), attribNumber);
			attribNumber++;

			attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}
	
	private void addShaderUniforms(String shaderText){
//		final HashMap<String, ArrayList<GLSLStruct>> structs = findUniformStructs(shaderText);
		
		final String UNIFORM_KEYWORD = "uniform";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		
		while(uniformStartLocation != -1){
			if(!(uniformStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(uniformStartLocation - 1)) || shaderText.charAt(uniformStartLocation - 1) == ';')
					&& Character.isWhitespace(shaderText.charAt(uniformStartLocation + UNIFORM_KEYWORD.length())))) {
				uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
				continue;
			}
			
			final int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
//			int end = shaderText.indexOf(";", begin);
			
//			String uniformLine = shaderText.substring(begin, end).trim();
			final String uniformLine = shaderText.substring(begin, shaderText.indexOf(";", begin)).trim();
			
			final int whiteSpacePos = uniformLine.indexOf(' ');
			final String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length()).trim();
			final String uniformType = uniformLine.substring(0, whiteSpacePos).trim();
			
			uniformNames.add(uniformName);
			uniformTypes.add(uniformType);
			addUniform(uniformName, uniformType, /*structs*/findUniformStructs(shaderText));
			
			uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}
	
	private void addUniform(String uniformName, String uniformType, HashMap<String, ArrayList<GLSLStruct>> structs){
		boolean addThis = true;
		final ArrayList<GLSLStruct> structComponents = structs.get(uniformType);
		
		if(structComponents != null){
			addThis = false;
			
			for(final GLSLStruct struct : structComponents){
				addUniform(uniformName + "." + struct.name, struct.type, structs);
			}
		}
		
		if(!addThis){
			return;
		}
		
		final int uniformLocation = GL20.glGetUniformLocation(program, uniformName);

		if(uniformLocation == /*0xFFFFFFFF*/-1){
			System.err.println("Error: Could not find uniform \"" + uniformName + "\" in " + fileName);
			new Exception().printStackTrace();
			System.exit(1);
		}

		uniformMap.put(uniformName, uniformLocation);
	}
	
	private void compileShader(){
		GL20.glLinkProgram(program);
		
		checkShaderError(program, GL20.GL_LINK_STATUS, true, "Error linking shader program");
		
//		if(glGetProgrami(program, GL_LINK_STATUS) == 0){
//			System.err.println(glGetShaderInfoLog(program, 1024));
//			System.exit(1);
//		}
		
		GL20.glValidateProgram(program);
		
		checkShaderError(program, GL20.GL_VALIDATE_STATUS, true, "Invalid shader program");
		
//		if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0){
//			System.err.println(glGetProgramInfoLog(program, 1024));
//			System.exit(1);
//		}
	}
	
	public void checkShaderError(int shader, int flag, boolean isProgram, String errorMessage){
		int success = 0;
	    String error;

		if(isProgram){
			success = GL20.glGetProgrami(shader, flag);
		}else{
			success = GL20.glGetShaderi(shader, flag);
		}
		
		if(success == 0){
			if(isProgram){
				error = GL20.glGetProgramInfoLog(shader, 1024)/*glGetProgramInfoLog(shader, sizeof(error), NULL, error)*/;
			}else{
				error = GL20.glGetShaderInfoLog(shader, 1024)/*glGetShaderInfoLog(shader, sizeof(error), NULL, error)*/;
			}
			
			System.err.println(errorMessage + ": " +error);
			System.exit(1);
		}
	}
	
	public int getProgram(){
		return program;
	}
	
	public List<Integer> getShaders(){
		return shaders;
	}
	
	public Map<String, Integer> getUniformMap(){
		return uniformMap;
	}
	
	@Override
	public List<String> getUniformNames(){
		return uniformNames;
	}
	
	@Override
	public List<String> getUniformTypes(){
		return uniformTypes;
	}
	
	private void setAttribLocation(String attribName, int location){
		GL20.glBindAttribLocation(program, location, attribName);
	}
	
	private static String loadShader(String fileName){
		final StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		final String INCLUDE_DIRECTIVE = "#include";
		
		try{
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			
			while((line = shaderReader.readLine()) != null){
				if(line.startsWith(INCLUDE_DIRECTIVE)){
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
				}else{
					shaderSource.append(line).append("\n");
				}
			}
			
			shaderReader.close();
		}catch(Exception e){
			e.printStackTrace();
//			System.exit(1);
		}
		
		return shaderSource.toString();
	}
	
	private class GLSLStruct{
		public String name;
		public String type;
	}
	
	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderText){
		final HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();

		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		
		while(structStartLocation != -1){
			if(!(structStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText.charAt(structStartLocation - 1) == ';')
					&& Character.isWhitespace(shaderText.charAt(structStartLocation + STRUCT_KEYWORD.length())))){
				structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
				continue;
			}

			final int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			final int braceBegin = shaderText.indexOf("{", nameBegin);
			final int braceEnd = shaderText.indexOf("}", braceBegin);

//			String structName = shaderText.substring(nameBegin, braceBegin).trim();
			final ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();

			int componentSemicolonPos = shaderText.indexOf(";", braceBegin);
			
			while(componentSemicolonPos != -1 && componentSemicolonPos < braceEnd){
				int componentNameEnd = componentSemicolonPos + 1;

				while(Character.isWhitespace(shaderText.charAt(componentNameEnd - 1)) || shaderText.charAt(componentNameEnd - 1) == ';'){
					componentNameEnd--;
				}

				int componentNameStart = componentSemicolonPos;

				while(!Character.isWhitespace(shaderText.charAt(componentNameStart - 1))){
					componentNameStart--;
				}

				int componentTypeEnd = componentNameStart;

				while(Character.isWhitespace(shaderText.charAt(componentTypeEnd - 1))){
					componentTypeEnd--;
				}

				int componentTypeStart = componentTypeEnd;

				while(!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1))){
					componentTypeStart--;
				}

//				String componentName = shaderText.substring(componentNameStart, componentNameEnd);
//				String componentType = shaderText.substring(componentTypeStart, componentTypeEnd);

				final GLSLStruct glslStruct = new GLSLStruct();
				glslStruct.name = shaderText.substring(componentNameStart, componentNameEnd);
				glslStruct.type = shaderText.substring(componentTypeStart, componentTypeEnd);

				glslStructs.add(glslStruct);

				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}
			
			result.put(shaderText.substring(nameBegin, braceBegin).trim(), glslStructs);
//			result.put(structName, glslStructs);

			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
		}

		return result;
	}
	
	@Override
	public void bind(){
//		if(bound != program){
			GL20.glUseProgram(program);
//			bound = program; 
//		}
	}
	
	@Override
	public void setUniformi(String uniformName, int value){
		GL20.glUniform1i(uniformMap.get(uniformName), value);
	}
	
	@Override
	public void setUniformf(String uniformName, float value){
		GL20.glUniform1f(uniformMap.get(uniformName), value);
	}
	
	@Override
	public void setUniformVector3f(String uniformName, Vector3f value){
		GL20.glUniform3f(uniformMap.get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	
//	@Override
//	public void setUniformVector2f(String uniformName, Vector2f value){
//		GL20.glUniform2f(uniformMap.get(uniformName), value.getX(), value.getY());
//	}
	
	@Override
	public void setUniformMatrix4f(String uniformName, Matrix4f value){
		GL20.glUniformMatrix4fv(uniformMap.get(uniformName), true, DataUtil.createFlippedBuffer(value));
	}
	
	@Override
	public void dispose(){
		for(final Integer it : shaders){
			GL20.glDetachShader(program, it);
			GL20.glDeleteShader(it);
		}
		
		GL20.glDeleteProgram(program);
	}
}
