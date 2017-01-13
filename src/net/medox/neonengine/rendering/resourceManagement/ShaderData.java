package net.medox.neonengine.rendering.resourceManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL20;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class ShaderData extends ReferenceCounter{
	private final int program;
	private final List<Integer> shaders;
	private final List<String> uniformNames;
	private final List<String> uniformTypes;
	private final Map<String, Integer> uniformMap;
	
	private final String fileName;
	
	public ShaderData(String fileName){
		super();
		
		this.fileName = fileName;
		
		this.program = GL20.glCreateProgram();
		
		if(program == 0){
			NeonEngine.throwError("Error: Shader creation failed: Could not find valid memory location in constructor.");
		}
		
		shaders = new ArrayList<Integer>();
		uniformNames = new ArrayList<String>();
		uniformTypes = new ArrayList<String>();
		uniformMap = new HashMap<String, Integer>();
		
		final String vetexShaderText = loadShader(fileName + ".vert");
		final String fragmentShaderText = loadShader(fileName + ".frag");
		
		addProgram(vetexShaderText, GL20.GL_VERTEX_SHADER);
		addProgram(fragmentShaderText, GL20.GL_FRAGMENT_SHADER);
		
		addAllAttributes(vetexShaderText);
		
		compileShader();
		
		addShaderUniforms(vetexShaderText);
		addShaderUniforms(fragmentShaderText);
	}
	
	private void addProgram(String text, int type){
		final int shader = GL20.glCreateShader(type);
		
		if(shader == 0){
			NeonEngine.throwError("Error: Shader creation failed: Could not find valid memory location when adding shader.");
		}
		
		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);
		
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0){
			NeonEngine.throwError(fileName + ": \n" + GL20.glGetShaderInfoLog(shader, 1024) + ".");
		}
		
		GL20.glAttachShader(program, shader);
		shaders.add(shader);
	}
	
	
	private void addAllAttributes(String shaderText){
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int attribNumber = 0;
		
		while(attributeStartLocation != -1){
			if(!(attributeStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText.charAt(attributeStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(attributeStartLocation + ATTRIBUTE_KEYWORD.length())))){
				attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
				continue;
			}
			
			final int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			
			final String attributeLine = shaderText.substring(begin, shaderText.indexOf(";", begin)).trim();
			
			setAttribLocation(attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim(), attribNumber);
			attribNumber++;
			
			attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}
	
	private void addShaderUniforms(String shaderText){
		final String UNIFORM_KEYWORD = "uniform";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		
		while(uniformStartLocation != -1){
			if(!(uniformStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(uniformStartLocation - 1)) || shaderText.charAt(uniformStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(uniformStartLocation + UNIFORM_KEYWORD.length())))){
				uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
				continue;
			}
			
			final int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			
			final String uniformLine = shaderText.substring(begin, shaderText.indexOf(";", begin)).trim();
			
			final int whiteSpacePos = uniformLine.indexOf(' ');
			String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length()).trim();
			final String uniformType = uniformLine.substring(0, whiteSpacePos).trim();
			
			int bracket = uniformName.indexOf('[');
			
			if(bracket >= 0){
				uniformName = uniformName.substring(0, bracket).trim();
			}
			
			uniformNames.add(uniformName);
			uniformTypes.add(uniformType);
			addUniform(uniformName, uniformType, findUniformStructs(shaderText));
			
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
		
		if(uniformLocation == -1){
			NeonEngine.throwError("Error: Could not find uniform \"" + uniformName + "\" in " + fileName + ".");
		}
		
		uniformMap.put(uniformName, uniformLocation);
	}
	
	private void compileShader(){
		GL20.glLinkProgram(program);
		
		checkShaderError(program, GL20.GL_LINK_STATUS, true, "Error linking shader program");
		
		GL20.glValidateProgram(program);
		
		checkShaderError(program, GL20.GL_VALIDATE_STATUS, true, "Invalid shader program");
	}
	
	private void checkShaderError(int shader, int flag, boolean isProgram, String errorMessage){
		int success = 0;
	    String error;

		if(isProgram){
			success = GL20.glGetProgrami(shader, flag);
		}else{
			success = GL20.glGetShaderi(shader, flag);
		}
		
		if(success == 0){
			if(isProgram){
				error = GL20.glGetProgramInfoLog(shader, 1024);
			}else{
				error = GL20.glGetShaderInfoLog(shader, 1024);
			}
			
			NeonEngine.throwError(errorMessage + ": " + error + ".");
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
	
	public List<String> getUniformNames(){
		return uniformNames;
	}
	
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
					if(line.contains("R_MAX_TEXTURE_IMAGE_UNITS")){
						line = line.replace("R_MAX_TEXTURE_IMAGE_UNITS", Integer.toString(RenderingEngine.maxTextureImageUnits));
					}
					
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
	
	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderText){
		final HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();
		
		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		
		while(structStartLocation != -1){
			if(!(structStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText.charAt(structStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(structStartLocation + STRUCT_KEYWORD.length())))){
				structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
				continue;
			}
			
			final int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			final int braceBegin = shaderText.indexOf("{", nameBegin);
			final int braceEnd = shaderText.indexOf("}", braceBegin);
			
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
				
				final GLSLStruct glslStruct = new GLSLStruct();
				glslStruct.name = shaderText.substring(componentNameStart, componentNameEnd);
				glslStruct.type = shaderText.substring(componentTypeStart, componentTypeEnd);
				
				glslStructs.add(glslStruct);
				
				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}
			
			result.put(shaderText.substring(nameBegin, braceBegin).trim(), glslStructs);
			
			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
		}
		
		return result;
	}
	
	public void bind(){
//		if(RenderingEngine.shaderBound != program){
			GL20.glUseProgram(program);
//			RenderingEngine.shaderBound = program;
//		}
	}
	
	public void setUniformi(String uniformName, int value){
		GL20.glUniform1i(uniformMap.get(uniformName), value);
	}
	
	public void setUniformf(String uniformName, float value){
		GL20.glUniform1f(uniformMap.get(uniformName), value);
	}
	
	public void setUniformVector3f(String uniformName, Vector3f value){
		GL20.glUniform3f(uniformMap.get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	
//	public void setUniformVector2f(String uniformName, Vector2f value){
//		GL20.glUniform2f(uniformMap.get(uniformName), value.getX(), value.getY());
//	}
	
	public void setUniformiVector(String uniformName, int[] value){
		GL20.glUniform1iv(uniformMap.get(uniformName), value);
	}
	
	public void setUniformMatrix4f(String uniformName, Matrix4f value){
		GL20.glUniformMatrix4fv(uniformMap.get(uniformName), true, DataUtil.createFlippedBuffer(value));
	}
	
	public void dispose(){
//		if(shaders != null){
			for(final Integer it : shaders){
				GL20.glDetachShader(program, it);
				GL20.glDeleteShader(it);
			}
			
//			shaders = null;
//		}
//		if(program != MemoryUtil.NULL){
			GL20.glDeleteProgram(program);
			
//			program = MemoryUtil.NULL;
//		}
	}
	
	private class GLSLStruct{
		public String name;
		public String type;
	}
}
