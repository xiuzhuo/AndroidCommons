package com.bonofa.cube7api;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import angel.zhuoxiu.library.net.HttpResult;
import angel.zhuoxiu.library.net.MultipartPost;

public class AttachManager implements Cube7Constants {
	public static final String TAG = AttachManager.class.getSimpleName();
	public static final String URL_API_UPLOAD = URL_HOST + " api/messages/create_attachment";

	public interface FileTransferListener{
		public void onStart();
		public void onTransfering();
		public void onFinish(boolean successful);
	}
	
	public interface AttachTransferListener extends FileTransferListener{
	}
	
	public static String uploadFile(String token, File file) {
		List<String> resultList = null;
		try {
			resultList = uploadFiles(token, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		} else {
			return null;
		}
	}

	public static List<String> uploadFiles(String token, File... files) {
		List<File> fileList = new ArrayList<File>();
		for (File file : files) {
			fileList.add(file);
		}
		return uploadFiles(token, fileList);
	}

	public static List<String> uploadFiles(String token, List<File> fileList) {
		Log.d(TAG, "getting token");
		HttpResult result = new HttpResult();
		List<String> resultList = new ArrayList<String>();
		if (fileList.size() > 0) {
			try {
				Log.d(TAG, "start uploading " + URL_API_UPLOAD + " token=" + token);
				for (File file : fileList) {
					if (file != null && file.exists() && file.isFile()) {
						MultipartPost post = new MultipartPost(URL_API_UPLOAD, "utf-8");
						post.setAuthBearerToken(token);
						post.addFilePart("file", file);
						result = post.execute();
						Log.i(TAG, "result upload=" + result);
						if (result.isOK()) {
							resultList.add(result.getEntityString());
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}
}
