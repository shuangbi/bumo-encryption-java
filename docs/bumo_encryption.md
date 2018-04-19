# __BUMO JAVA ENCRYPTIOIN使用文档__

## 1 用途
用于生成公私钥和地址，以及签名，和验签，只支持ED25519和SM2，默认是ED25519。

## 2 jar包引用
所依赖的jar包在jar文件夹中寻找，依赖的jar包如下：

1. bumo-encryption-1.0.0.jar:用于生成公私钥和地址，以及签名，和验签，详细请看下面介绍
2. sadk-3.2.3.0.RELEASE.jar:用于SM2的签名操作

## 2 构造对象
### 2.1 构造PrivateKey对象
#### 2.1.1 签名方式构造

示例：
```java
PrivateKey privateKey = new PrivateKey(KeyType.ED25519);
````

### 2.1.2 私钥构造
参数是编码后的私钥

示例如下：
```java
String encPrivateKey;
PrivateKey privateKey = new PrivateKey(encPrivateKey);
```

### 2.2 构造PublicKey对象
#### 2.2.1 公钥构造
参数是编码的公钥

示例如下：
```java
String encPublicKey;
PublicKey publicKey = new PublicKey(encPublicKey);
```

### 3 接口详细描述
#### 3.1签名（非静态）
方法名: sign
注意：调用此方法需要构造PrivateKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| msg | byte[] | 待签名信息

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| signMsg | byte[] | 签名后信息

例如：
```java
PrivateKey privateKey = PrivateKey(KeyType.ED25519);
String src = "test";
byte[] signMsg = privateKey.sign(src.getBytes());
```

#### 3.2 签名（静态）
方法名: sign
注意：调用此方法不需要构造PrivateKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| msg | byte[] | 待签名信息
| privateKey | String | 私钥

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| signMsg | byte[] | 签名后信息

例如：
```java
String src = "test";
String privateKey;
byte[] sign = PrivateKey.sign(src.getBytes(), privateKey);
```

#### 3.3 验签（非静态）
方法名: verify
注意：调用此方法需要构造PublicKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| msg | byte[] | 签名原信息
| signMsg | byte[] | 签名后信息

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| verify | boolean | 验签结果

例如：
```java
String publicKey = "";
PublicKey publicKey = new PublicKey(publicKey);

String sign = "";
String src = "test";
Boolean verifyResult = publicKey.verify(src.getBytes(), sign.getBytes());
```

##### 3.4 验签（静态）
方法名: verify
注意：调用此方法不需要构造PublicKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| msg | byte[] | 签名原信息
| signMsg | byte[] | 签名后信息
| publicKey | String | 公钥

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| verify | boolean | 验签结果

例如：
```java
String src = "test";
String publicKey;
String sign = "";
Boolean verifyResult = PublicKey.verify(src.getBytes(), sign, publicKey);
```


#### 3.5 获取私钥（非静态）
方法名：getEncPrivateKey
注意：调用此方法需要构造PrivateKey对象

请求参数：无

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| encPrivateKey | String | 编码后的私钥

例如：
```java
PrivateKey privateKey = new PrivateKey(KeyType.ECCSM2);
String encPrivateKey = privateKey.getEncPrivateKey();
```

#### 3.6 获取公钥（非静态）
方法名：getEncPublicKey
注意：调用此方法需要构造PrivateKey对象

请求参数：无

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| encPublicKey | String | 编码后的公钥

例如：
```java
PrivateKey privateKey = new PrivateKey(KeyType.ECCSM2);
String encPublicKey = privateKey.getEncPublicKey();
```

#### 3.7 获取公钥（静态）
方法名：getEncPublicKey
注意：调用此方法不需要构造PrivateKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| EncPrivateKey | String | 编码的私钥

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| encPublicKey | String | 编码的公钥

例如：
```java
String encPrivateKey;
String encPublicKey = PrivateKey.getEncPublicKey(encPrivateKey);
```

#### 3.8 获取地址（非静态）
方法名：getEncAddress
注意：调用此方法需要构造PublicKey对象

请求参数： 无

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| encAddress | String | 编码的地址

例如：
```java
String encPublicKey = "";
PublicKey publicKey = new PublicKey(encPublicKey);
String encAddress = publicKey.getEncAddress();
```

#### 3.9 获取地址（静态）
方法名：getEncAddress
注意：调用此方法不需要构造PublicKey对象

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| encPrivateKey | String | 编码的私钥

返回结果：

|变量|类型|描述
|:--- | --- | --- 
| encAddress | String | 编码后的地址

例如：
```java
String encPublicKey;
String encAddress = PublicKey.getEncAddress(encPublicKey);
```

#### 3.12 计算hash
方法名：GenerateHashHex
路径：org.bumo.encryption.utils.HashUtil

请求参数：

|变量|类型|描述
|:--- | --- | --- 
| src | byte[] | 待计算的字节数组，即交易的序列化字节数组
| type | Integer | hash类型,0是sha-256,1是sm3

例如
```java
String url="127.0.0.1:19333";
String getHello = url + "/hello";
String hello = HttpKit.post(getHello, "");
JSONObject ho = JSONObject.parseObject(hello);
Integer hash_type = ho.containsKey("hash_type") ? ho.getInteger("hash_type") : 0;
Transaction.Builder tran = Transaction.newBuilder();
String hash = HashUtil.GenerateHashHex(tran.build().toByteArray(), hash_type);
```

若要获取bumo底层的hash类型，需要访问http的hello接口，会返回hash类型

### 4 举例说明
#### 4.1 创建账户
测试例子如下：

```java
public static PrivateKey TestCreateAccount(String url, String srcAddress, String srcPrivate, String srcPublic, String signerAddress, String signerPrivate, String signerPublic, KeyType algorithm) {
    PrivateKey bumokey_new = null;
    try {
    	// getAccount
    	String getAccount = url + "/getAccount?address=" + srcAddress;
    	String txSeq = HttpKit.post(getAccount, "");
    	JSONObject tx = JSONObject.parseObject(txSeq);
    	String seq_str = tx.getJSONObject("result").containsKey("nonce") ? tx.getJSONObject("result").getString("nonce") : "0";
    	long nonce = Long.parseLong(seq_str);
    			
    	// generate new Account address, PrivateKey, publicKey
    	bumokey_new = new PrivateKey(algorithm);
    	String newAccountAddress = bumokey_new.getEncAddress();
    			
    	// use src account sign
    	PrivateKey bumoKey_sign = new PrivateKey(signerPrivate);
    			
    	JSONObject transaction = new JSONObject();
    	transaction.put("source_address", srcAddress);
    	transaction.put("nonce", nonce + 1);
    	transaction.put("fee_limit", 1000000);
    	transaction.put("gas_price", 1000);
    	JSONArray operations = new JSONArray();
    	JSONObject operation = new JSONObject();
    	operation.put("type", 1);
    	JSONObject createAccount = new JSONObject();
    	JSONObject priv = new JSONObject();
    	priv.put("master_weight", 1);
    	JSONObject thresholds = new JSONObject();
    	thresholds.put("tx_threshold", 1);
    			
    	createAccount.put("dest_address", newAccountAddress);
    	createAccount.put("init_balance", 1000000000000L);
    	priv.put("thresholds", thresholds);
    	createAccount.put("priv", priv);
    	operation.put("create_account", createAccount);
    	operations.add(operation);
    	transaction.put("operations", operations);
    	String getTransactionBlob = url + "/getTransactionBlob";
    	String blob = HttpKit.post(getTransactionBlob, transaction.toJSONString());
    	JSONObject transactionBlob = JSON.parseObject(blob);
    	long error_code = transactionBlob.getLongValue("error_code");
    	JSONObject blobResult = transactionBlob.getJSONObject("result");
    	if (transactionBlob != null && error_code != 0) {
    		String hash = blobResult.getString("hash");
    		String desc = transactionBlob.getString("error_desc");
    		System.out.println("create account blob (" + hash + ") error description: " + desc);
    		return null;
    	}
    	String blob_hex = blobResult.getString("transaction_blob");
    			
    	// add transaction with signature
    	JSONObject request = new JSONObject();
    	JSONArray items = new JSONArray();
    	JSONObject item = new JSONObject();
    	item.put("transaction_blob", blob_hex);
    	JSONArray signatures = new JSONArray();
    	JSONObject signature = new JSONObject();
    	signature.put("sign_data", HexFormat.byteToHex(bumoKey_sign.sign(HexFormat.hexToByte(blob_hex))));
    	signature.put("public_key", signerPublic);
    	signatures.add(signature);
    	item.put("signatures", signatures);
    	items.add(item);
    	request.put("items", items);
    			
    	String submitTransaction = url + "/submitTransaction";
    	String trans = HttpKit.post(submitTransaction, request.toJSONString());
    	JSONObject transObj = JSONObject.parseObject(trans);
    	JSONArray transResult = transObj.getJSONArray("results");
    	String hash = transResult.getJSONObject(0).getString("hash");
    	if (transResult.getJSONObject(0).getLongValue("error_code") != 0) {
    		String desc = transResult.getJSONObject(0).getString("error_desc");
    		System.out.println("create account transaction(" + hash + ") error description: " + desc);
    		return null;
    	}
    	System.out.println("create account transaction hash (" + hash + ")");
    } catch (Exception e) {
    	e.printStackTrace();
    }
		
    return bumokey_new;
}
```

调用测试例子：

```java
String url = "http://127.0.0.1:36002";
String privateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
String publicKey = "b001b6d3120599d19cae7adb6c5e2674ede8629c871cb8b93bd05bb34d203cd974c3f0bc07e5";
String address = "buQdBdkvmAhnRrhLp4dmeCc2ft7RNE51c9EK";
PrivateKey bumoKey = TestCreateAccount(url, address, privateKey, publicKey, address, privateKey, publicKey, KeyType.ED25519);
System.out.println(bumoKey);
```

#### 4.2 发行资产
测试例子如下：
```java
public static PrivateKey TestIssueAsset(String url, String address, String privateKey, String publicKey, String code, long amount) {
	PrivateKey bumokey_new = null;
	try {
		// getAccount
		String getAccount = url + "/getAccount?address=" + address;
		String txSeq = HttpKit.post(getAccount, "");
		JSONObject tx = JSONObject.parseObject(txSeq);
		String seq_str = tx.getJSONObject("result").containsKey("nonce") ? tx.getJSONObject("result").getString("nonce") : "0";
		long nonce = Long.parseLong(seq_str);
			
		// use src account sign
		PrivateKey bumoKey_sign = new PrivateKey(privateKey);
			
		JSONObject transaction = new JSONObject();
		transaction.put("source_address", address);
		transaction.put("nonce", nonce + 1);
		transaction.put("fee_limit", 6000000000L);
		transaction.put("gas_price", 1000);
		JSONArray operations = new JSONArray();
		JSONObject operation = new JSONObject();
		operation.put("type", 2);
		JSONObject issueAsset = new JSONObject();
		issueAsset.put("code", code);
		issueAsset.put("amount", amount);
			
		operation.put("issue_asset", issueAsset);
		operations.add(operation);
		transaction.put("operations", operations);
		String getTransactionBlob = url + "/getTransactionBlob";
		String blob = HttpKit.post(getTransactionBlob, transaction.toJSONString());
		JSONObject transactionBlob = JSON.parseObject(blob);
		long error_code = transactionBlob.getLongValue("error_code");
		JSONObject blobResult = transactionBlob.getJSONObject("result");
		if (transactionBlob != null && error_code != 0) {
			String hash = blobResult.getString("hash");
			String desc = transactionBlob.getString("error_desc");
			System.out.println("issue asset blob (" + hash + ") error description: " + desc);
			return null;
		}
		String blob_hex = blobResult.getString("transaction_blob");
			
		// add transaction with signature
		JSONObject request = new JSONObject();
		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		item.put("transaction_blob", blob_hex);
		JSONArray signatures = new JSONArray();
		JSONObject signature = new JSONObject();
		signature.put("sign_data", HexFormat.byteToHex(bumoKey_sign.sign(HexFormat.hexToByte(blob_hex))));
		signature.put("public_key", publicKey);
		signatures.add(signature);
		item.put("signatures", signatures);
		items.add(item);
		request.put("items", items);
			
		String submitTransaction = url + "/submitTransaction";
		String trans = HttpKit.post(submitTransaction, request.toJSONString());
		JSONObject transObj = JSONObject.parseObject(trans);
		JSONArray transResult = transObj.getJSONArray("results");
		String hash = transResult.getJSONObject(0).getString("hash");
		if (transResult.getJSONObject(0).getLongValue("error_code") != 0) {
			String desc = transResult.getJSONObject(0).getString("error_desc");
			System.out.println("issue asset transaction(" + hash + ") error description: " + desc);
			return null;
		}
		System.out.println("issue asset transaction hash (" + hash + ")");
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	return bumokey_new;
}
```

调用测试例子：

```java
String url = "http://127.0.0.1:36002";
String privateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
String publicKey = "b001b6d3120599d19cae7adb6c5e2674ede8629c871cb8b93bd05bb34d203cd974c3f0bc07e5";
String address = "buQdBdkvmAhnRrhLp4dmeCc2ft7RNE51c9EK";
TestIssueAsset(url, bumoKey.getEncAddress(), bumoKey.getEncPrivateKey(), bumoKey.getEncPublicKey(), "CNY", 10000);
```

#### 4.3 转移资产
测试例子如下：

```java
public static PrivateKey TestPayment(String url, String issueAddress, String srcAddress, String srcPrivate, String srcPublic, String destAddress, String code, long amount) {
	PrivateKey bumokey_new = null;
	try {
		// getAccount
		String getAccount = url + "/getAccount?address=" + srcAddress;
		String txSeq = HttpKit.post(getAccount, "");
		JSONObject tx = JSONObject.parseObject(txSeq);
		String seq_str = tx.getJSONObject("result").containsKey("nonce") ? tx.getJSONObject("result").getString("nonce") : "0";
		long nonce = Long.parseLong(seq_str);
			
		// use src account sign
		PrivateKey bumoKey_sign = new PrivateKey(srcPrivate);
			
		JSONObject transaction = new JSONObject();
		transaction.put("source_address", srcAddress);
		transaction.put("nonce", nonce + 1);
		transaction.put("fee_limit", 1000000);
		transaction.put("gas_price", 1000);
		JSONArray operations = new JSONArray();
		JSONObject operation = new JSONObject();
		operation.put("type", 3);
		JSONObject payment = new JSONObject();
		payment.put("dest_address", destAddress);
		JSONObject asset = new JSONObject();
		JSONObject key = new JSONObject();
		key.put("issuer", issueAddress);
		key.put("code", code);
		key.put("type", 0);
			
		asset.put("key", key);
		asset.put("amount", amount);
		operation.put("asset", asset);
		operation.put("payment", payment);
		operations.add(operation);
		transaction.put("operations", operations);
		String getTransactionBlob = url + "/getTransactionBlob";
		String blob = HttpKit.post(getTransactionBlob, transaction.toJSONString());
		JSONObject transactionBlob = JSON.parseObject(blob);
		long error_code = transactionBlob.getLongValue("error_code");
		JSONObject blobResult = transactionBlob.getJSONObject("result");
		if (transactionBlob != null && error_code != 0) {
			String hash = blobResult.getString("hash");
			String desc = transactionBlob.getString("error_desc");
			System.out.println("payment blob (" + hash + ") error description: " + desc);
			return null;
		}
		String blob_hex = blobResult.getString("transaction_blob");
			
		// add transaction with signature
		JSONObject request = new JSONObject();
		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		item.put("transaction_blob", blob_hex);
		JSONArray signatures = new JSONArray();
		JSONObject signature = new JSONObject();
		signature.put("sign_data", HexFormat.byteToHex(bumoKey_sign.sign(HexFormat.hexToByte(blob_hex))));
		signature.put("public_key", srcPublic);
		signatures.add(signature);
		item.put("signatures", signatures);
		items.add(item);
		request.put("items", items);
			
		String submitTransaction = url + "/submitTransaction";
		String trans = HttpKit.post(submitTransaction, request.toJSONString());
		JSONObject transObj = JSONObject.parseObject(trans);
		JSONArray transResult = transObj.getJSONArray("results");
		String hash = transResult.getJSONObject(0).getString("hash");
		if (transResult.getJSONObject(0).getLongValue("error_code") != 0) {
			String desc = transResult.getJSONObject(0).getString("error_desc");
			System.out.println("payment transaction(" + hash + ") error description: " + desc);
			return null;
		}
		System.out.println("payment transaction hash (" + hash + ")");
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	return bumokey_new;
}
```

调用测试例子：

```java
String url = "http://127.0.0.1:36002";
String privateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
String publicKey = "b001b6d3120599d19cae7adb6c5e2674ede8629c871cb8b93bd05bb34d203cd974c3f0bc07e5";
String address = "buQdBdkvmAhnRrhLp4dmeCc2ft7RNE51c9EK";
TestPayment(url, bumoKey.getEncAddress(), bumoKey.getEncAddress(), bumoKey.getEncPrivateKey(), bumoKey.getEncPublicKey(), address, "CNY", 5000);
```

#### 4.3 转移BU币
测试例子如下 ：

```java
public static PrivateKey TestPayCoin(String url, String srcAddress, String srcPrivate, String srcPublic, String destAddress, long amount) {
	PrivateKey bumokey_new = null;
	try {
		// getAccount
		String getAccount = url + "/getAccount?address=" + srcAddress;
		String txSeq = HttpKit.post(getAccount, "");
		JSONObject tx = JSONObject.parseObject(txSeq);
		String seq_str = tx.getJSONObject("result").containsKey("nonce") ? tx.getJSONObject("result").getString("nonce") : "0";
		long nonce = Long.parseLong(seq_str);
			
		// use src account sign
		PrivateKey bumoKey_sign = new PrivateKey(srcPrivate);
			
		JSONObject transaction = new JSONObject();
		transaction.put("source_address", srcAddress);
		transaction.put("nonce", nonce + 1);
		transaction.put("fee_limit", 1000000);
		transaction.put("gas_price", 1000);
		JSONArray operations = new JSONArray();
		JSONObject operation = new JSONObject();
		operation.put("type", 7);
		JSONObject payCoin = new JSONObject();
		payCoin.put("dest_address", destAddress);
		payCoin.put("amount", amount);
			
		operation.put("pay_coin", payCoin);
		operations.add(operation);
		transaction.put("operations", operations);
		String getTransactionBlob = url + "/getTransactionBlob";
		String blob = HttpKit.post(getTransactionBlob, transaction.toJSONString());
		JSONObject transactionBlob = JSON.parseObject(blob);
		long error_code = transactionBlob.getLongValue("error_code");
		JSONObject blobResult = transactionBlob.getJSONObject("result");
		if (transactionBlob != null && error_code != 0) {
			String hash = blobResult.getString("hash");
			String desc = transactionBlob.getString("error_desc");
			System.out.println("pay coin blob (" + hash + ") error description: " + desc);
			return null;
		}
		String blob_hex = blobResult.getString("transaction_blob");
			
		// add transaction with signature
		JSONObject request = new JSONObject();
		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		item.put("transaction_blob", blob_hex);
		JSONArray signatures = new JSONArray();
		JSONObject signature = new JSONObject();
		signature.put("sign_data", HexFormat.byteToHex(bumoKey_sign.sign(HexFormat.hexToByte(blob_hex))));
		signature.put("public_key", srcPublic);
		signatures.add(signature);
		item.put("signatures", signatures);
		items.add(item);
		request.put("items", items);
			
		String submitTransaction = url + "/submitTransaction";
		String trans = HttpKit.post(submitTransaction, request.toJSONString());
		JSONObject transObj = JSONObject.parseObject(trans);
		JSONArray transResult = transObj.getJSONArray("results");
		String hash = transResult.getJSONObject(0).getString("hash");
		if (transResult.getJSONObject(0).getLongValue("error_code") != 0) {
			String desc = transResult.getJSONObject(0).getString("error_desc");
			System.out.println("pay coin transaction(" + hash + ") error description: " + desc);
			return null;
		}
		System.out.println("pay coin transaction hash (" + hash + ")");
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	return bumokey_new;
}
```

调用测试例子：

```java
String url = "http://127.0.0.1:36002";
String privateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
String publicKey = "b001b6d3120599d19cae7adb6c5e2674ede8629c871cb8b93bd05bb34d203cd974c3f0bc07e5";
String address = "buQdBdkvmAhnRrhLp4dmeCc2ft7RNE51c9EK";
TestPayCoin(url, address, privateKey, publicKey, bumoKey.getEncAddress(), 50000);
```