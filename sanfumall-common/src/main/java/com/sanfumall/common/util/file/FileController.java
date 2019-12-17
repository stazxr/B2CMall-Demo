package com.sanfumall.common.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sanfumall.common.base.controller.BaseController;

/**
 * 异步上传组件
 * @author SunTao
 * @since 2018-12-21
 */
@Controller("fileController")
@RequestMapping("/file")
public class FileController extends BaseController{
	
	/**
	 * 上传用户头像
	 * @return String fileName
	 * @throws Exception
	 */
	@RequestMapping(value="/userImgUpload", method=RequestMethod.POST)
	@ResponseBody
	public String uploadUserImgFile(@RequestParam("uploadUserImg")MultipartFile multipartFile) throws Exception {
		// 第一步：获得上传文件的原始名字
		String origFileName = multipartFile.getOriginalFilename();
		// 第二步：截取原始名字获得其文件后缀（如".jpg"）
		String suffix = origFileName.substring(origFileName.lastIndexOf("."));
		// 第三步：根据系统生成随机数，结合后缀,生成新的文件名
		String fileName = System.currentTimeMillis() + suffix;
		// 第四步：获得当前服务器路径
		String path = session.getServletContext().getRealPath("/static/images/userImg");
		// 第五步：判断当前文件夹是否存在，如果不存在，则创建文件夹
		File uploadFold = new File(path);
		if (!uploadFold.exists()) {
			uploadFold.mkdirs();
		}
		// 第六步：获得完整的文件名（路径加文件名）
		String wholeFileName = path + File.separator + fileName;
		// 第七步：通过完整的文件名获得对应的输出流（根据完整的文件名将图片写入到文件夹中）
		OutputStream os = new FileOutputStream(new File(wholeFileName));
		// 第八步： 获得输入流，并将字节从输入流复制到输出流（进行照片的回显）
		InputStream is = multipartFile.getInputStream();
		IOUtils.copy(is, os);
		// 第九步：关闭流，返回文件名
		is.close();
		os.close();
		return fileName;
	}
	
	/**
	 * 上传支付类型图标
	 * @return String fileName
	 * @throws Exception
	 */
	@RequestMapping(value="/paymentIconUpload", method=RequestMethod.POST)
	@ResponseBody
	public String uploadPaymentIcon(@RequestParam("paymentIconImg")MultipartFile multipartFile) throws Exception {
		// 第一步：获得上传文件的原始名字
		String origFileName = multipartFile.getOriginalFilename();
		// 第二步：截取原始名字获得其文件后缀（如".jpg"）
		String suffix = origFileName.substring(origFileName.lastIndexOf("."));
		// 第三步：根据系统生成随机数，结合后缀,生成新的文件名
		String fileName = System.currentTimeMillis() + suffix;
		// 第四步：获得当前服务器路径
		String path = session.getServletContext().getRealPath("/static/images/paymentIcon");
		// 第五步：判断当前文件夹是否存在，如果不存在，则创建文件夹
		File uploadFold = new File(path);
		if (!uploadFold.exists()) {
			uploadFold.mkdirs();
		}
		// 第六步：获得完整的文件名（路径加文件名）
		String wholeFileName = path + File.separator + fileName;
		// 第七步：通过完整的文件名获得对应的输出流（根据完整的文件名将图片写入到文件夹中）
		OutputStream os = new FileOutputStream(new File(wholeFileName));
		// 第八步： 获得输入流，并将字节从输入流复制到输出流（进行照片的回显）
		InputStream is = multipartFile.getInputStream();
		IOUtils.copy(is, os);
		// 第九步：关闭流，返回文件名
		is.close();
		os.close();
		return fileName;
	}
	
	/**
	 * 添加修改商品时通过富文本编辑器上传商品详情
	 * @return Map<String, String>
	 * @throws Exception
	 */
	@RequestMapping(value="/productUpload", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadProductFile(@RequestParam("uploadProduct")MultipartFile multipartFile) throws Exception {
		// 第一步：获得上传文件的原始名字
		String origFileName = multipartFile.getOriginalFilename();
		// 第二步：截取原始名字获得其文件后缀（如".jpg"）
		String suffix = origFileName.substring(origFileName.lastIndexOf("."));
		// 第三步：根据系统生成随机数，结合后缀,生成新的文件名
		String fileName = System.currentTimeMillis() + suffix;
		// 第四步：获得当前服务器路径
		String path = session.getServletContext().getRealPath("/static/images/productImg/detail");
		// 第五步：判断当前文件夹是否存在，如果不存在，则创建文件夹
		File uploadFold = new File(path);
		if (!uploadFold.exists()) {
			uploadFold.mkdirs();
		}
		// 第六步：获得完整的文件名（路径加文件名）
		String wholeFileName = path + File.separator + fileName;
		// 第七步：通过完整的文件名获得对应的输出流（根据完整的文件名将图片写入到文件夹中）
		OutputStream os = new FileOutputStream(new File(wholeFileName));
		// 第八步： 获得输入流，并将字节从输入流复制到输出流（进行照片的回显）
		InputStream is = multipartFile.getInputStream();
		IOUtils.copy(is, os);
		// 第九步：关闭流，返回文件名
		is.close();
		os.close();
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", fileName);
		return map;
	}
	
	/**
	 * 添加修改商品时手动上传商品图片
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/productImgUpload", method=RequestMethod.POST)
	@ResponseBody
	public String uploadProductImgFile(@RequestParam("uploadProductImg")MultipartFile multipartFile) throws Exception {
		// 第一步：获得上传文件的原始名字
		String origFileName = multipartFile.getOriginalFilename();
		// 第二步：截取原始名字获得其文件后缀（如".jpg"）
		String suffix = origFileName.substring(origFileName.lastIndexOf("."));
		// 第三步：根据系统生成随机数，结合后缀,生成新的文件名
		String fileName = System.currentTimeMillis() + suffix;
		// 第四步：获得当前服务器路径
		String path = session.getServletContext().getRealPath("/static/images/productImg");
		// 第五步：判断当前文件夹是否存在，如果不存在，则创建文件夹
		File uploadFold = new File(path);
		if (!uploadFold.exists()) {
			uploadFold.mkdirs();
		}
		// 第六步：获得完整的文件名（路径加文件名）
		String wholeFileName = path + File.separator + fileName;
		// 第七步：通过完整的文件名获得对应的输出流（根据完整的文件名将图片写入到文件夹中）
		OutputStream os = new FileOutputStream(new File(wholeFileName));
		// 第八步： 获得输入流，并将字节从输入流复制到输出流（进行照片的回显）
		InputStream is = multipartFile.getInputStream();
		IOUtils.copy(is, os);
		// 第九步：关闭流，返回文件名
		is.close();
		os.close();
		return fileName;
	}
	
}