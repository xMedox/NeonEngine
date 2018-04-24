package net.medox.neonengine.rendering.meshLoading.colladaLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlNode{
	private String name;
	private Map<String, String> attributes;
	private String data;
	private Map<String, List<XmlNode>> childNodes;
	
	protected XmlNode(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getData(){
		return data;
	}
	
	public String getAttribute(String attr){
		if(attributes != null){
			return attributes.get(attr);
		}else{
			return null;
		}
	}
	
	public XmlNode getChild(String childName){
		if(childNodes != null){
			List<XmlNode> nodes = childNodes.get(childName);
			if(nodes != null && !nodes.isEmpty()){
				return nodes.get(0);
			}
		}
		return null;

	}
	
	public XmlNode getChildWithAttribute(String childName, String attr, String value){
		List<XmlNode> children = getChildren(childName);
		if(children == null || children.isEmpty()){
			return null;
		}
		for(XmlNode child : children){
			String val = child.getAttribute(attr);
			if(value.equals(val)){
				return child;
			}
		}
		return null;
	}
	
	public List<XmlNode> getChildren(String name){
		if(childNodes != null){
			List<XmlNode> children = childNodes.get(name);
			if(children != null){
				return children;
			}
		}
		return new ArrayList<XmlNode>();
	}
	
	protected void addAttribute(String attr, String value){
		if(attributes == null){
			attributes = new HashMap<String, String>();
		}
		attributes.put(attr, value);
	}
	
	protected void addChild(XmlNode child){
		if(childNodes == null){
			childNodes = new HashMap<String, List<XmlNode>>();
		}
		List<XmlNode> list = childNodes.get(child.name);
		if(list == null){
			list = new ArrayList<XmlNode>();
			childNodes.put(child.name, list);
		}
		list.add(child);
	}
	
	protected void setData(String data){
		this.data = data;
	}
}
