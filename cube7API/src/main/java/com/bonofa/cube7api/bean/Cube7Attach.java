package com.bonofa.cube7api.bean;

import java.io.File;
import java.io.FileNotFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.bonofa.cube7api.AttachManager;

/**
	https://github.com/c7devteam/cube7/blob/ejabberd/doc_api/message_attachments.md
	{
	  id: 1
	  success: true
	  created_at: "2014-11-11T15:25:42.902+01:00"
	  file_name: "wmeLEDiTs48.jpg"
	  thumb_url: "http://asdasd.com/1.jpg"
	  file_url: "https://c7dev.s3.amazonaws.com/uploads/message_attachment/file/176/wmeLEDiTs48.jpg?v=1415715942"
	  file_size: 277077
	}
*/
public class Cube7Attach {
	@JSONField(
			serialize = false, deserialize = false)
	private String uploadJSONText;
	@JSONField(
			serialize = false, deserialize = false)
	private String file_path;

	private long id;
	private boolean success = false;
	private long created_at;
	private String file_name;
	private String thumb_url;
	private String file_url;
	private long file_size;

	public Cube7Attach(File file) throws FileNotFoundException {
		if (fileExist(file)) {
			this.file_path = file.getAbsolutePath();
		} else {
			throw new FileNotFoundException();
		}
	}

	public Cube7Attach(String file_path) throws FileNotFoundException {
		this(new File(file_path));
	}

	public static Cube7Attach createFromJSON(String uploadJSONText) {
		Cube7Attach attach = JSON.parseObject(uploadJSONText, Cube7Attach.class, Feature.AllowISO8601DateFormat);
		attach.uploadJSONText = uploadJSONText;
		return attach;
	}

	public Cube7Attach parseToSelf(String text) {
		Cube7Attach attach = Cube7Attach.createFromJSON(text);
		if (attach.success) {
			this.success = attach.success;
			if (attach.id != 0) {
				this.id = attach.id;
			}
			if (attach.created_at != 0) {
				this.created_at = attach.created_at;
			}
			if (attach.file_name != null) {
				this.file_name = attach.file_name;
			}
			if (attach.thumb_url != null) {
				this.thumb_url = attach.thumb_url;
			}
			if (attach.file_url != null) {
				this.file_url = attach.file_url;
			}
			if (attach.file_size != 0) {
				this.file_size = attach.file_size;
			}
			if (attach.file_path != null) {
				this.file_path = attach.file_path;
			}
			if (attach.uploadJSONText != null) {
				this.uploadJSONText = attach.uploadJSONText;
			}
		}
		return this;
	}

	public boolean upload(String token) {
		this.success = false;
		if (fileExist()) {
			String result = AttachManager.uploadFile(token, getFile());
			this.parseToSelf(result);
		}
		return this.success;
	}

	File getFile() {
		return new File(file_name);
	}

	boolean fileExist() {
		if (file_path == null) {
			return false;
		}
		return fileExist(new File(file_path));
	}

	static boolean fileExist(File file) {
		return (file != null && file.exists() && file.isFile());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public long getFile_size() {
		return file_size;
	}

	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
}
