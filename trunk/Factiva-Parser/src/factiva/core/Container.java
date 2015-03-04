package factiva.core;

public class Container {
	private String tag = "", content = "";
	public Container(){
		clear();
	}
	public void clear(){
		clearTag();
		clearContent();
	}
	
	public void clearTag(){
		this.tag = "";
	}
	
	public void clearContent(){
		this.content = "";
	}
	
	public void updateTag(String tag){
		this.tag = tag;
	}
	
	public void attachContent(String content){
		this.content += content;
	}
	
	public boolean hasTag(){
		if(tag.length() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean hasContent(){
		if(content.length() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public String tag(){
		return this.tag;
	}
	
	public String content(){
		return this.content;
	}
}
