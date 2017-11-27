/**
 * MD5散列时在明文上附加的混淆KEY
 */
var MD5KEY = "DD8AA8E5AA5F4335A7ABD95671319231";
/**
 * 表示没有设置密码时的密码明文字符串
 */
var NOPWD = "nopassword_0a60636d2da743858909d451f2fb6c7e";
/**
 * RSA公钥
 */
var PUBKEY = "82991051cf8a147a1b2e1eb658f4c62961797c499db087c6bb3e01aee9a6e40debe0eb1589e51b8e069ae722d61953ef1c16c3459bfff5aa77b8419991fc0eb3";
var md5pwd;
/**
 * 将明文字符串按照RSA加密密码的方式进行加密
 * @param userName 用户名
 * @param password 明文字符串
 * @returns 密文BASE64字符串
 */
function asRsaPassword(userName, password) {
	var rsa = new RSAKey();
	rsa.setPublic(PUBKEY, "10001");
	if (password.length < 1) {
		md5pwd = b64_md5(MD5KEY + userName + NOPWD);
	} else {
		md5pwd = b64_md5(MD5KEY + userName + password);
	}
	return hex2b64(rsa.encrypt(md5pwd));
}
/**
 * 将明文字符串按照MD5散列密码的方式进行编码
 * @param userName 用户名
 * @param password 明文字符串
 * @returns BASE64字符串
 */
function asMd5Password(userName, password) {
	if (password.length < 1) {
		return b64_md5(MD5KEY + userName + (b64_md5(MD5KEY + userName + NOPWD)));
	}
	return b64_md5(MD5KEY + userName + (b64_md5(MD5KEY + userName + password)));
}
