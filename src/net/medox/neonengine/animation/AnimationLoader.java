package net.medox.neonengine.animation;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.XmlNode;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.XmlParser;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.AnimationData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.JointTransformData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.KeyFrameData;

public class AnimationLoader{
	public static Animation loadAnimation(String filename){
		XmlNode node = XmlParser.loadXmlFile("./res/models/" + filename);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		net.medox.neonengine.rendering.meshLoading.colladaLoading.AnimationLoader loader = new net.medox.neonengine.rendering.meshLoading.colladaLoading.AnimationLoader(animNode, jointsNode);
		AnimationData animationData = loader.extractAnimation();
		
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for(int i = 0; i < frames.length; i++){
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(animationData.lengthSeconds, frames);
	}
	
	private static KeyFrame createKeyFrame(KeyFrameData data){
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for(JointTransformData jointData : data.jointTransforms){
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}
	
	private static JointTransform createTransform(JointTransformData data){
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.get(3, 0), mat.get(3, 1), mat.get(3, 2));
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}
}
