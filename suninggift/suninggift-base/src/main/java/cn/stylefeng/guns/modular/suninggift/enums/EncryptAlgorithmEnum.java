package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 
 *加密算法枚举类
 */
public enum EncryptAlgorithmEnum {
	
	RSA2("RSA2"),
	MD5("MD5");
	
	private String algorithmName;
	
	private EncryptAlgorithmEnum(String algorithmName){
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	
	public static EncryptAlgorithmEnum getEncryptAlgorithmEnum(String algorithmName){
		EncryptAlgorithmEnum[] encryptAlgorithmEnumArray = EncryptAlgorithmEnum.values();
		for(EncryptAlgorithmEnum encryptAlgorithmEnum : encryptAlgorithmEnumArray){
			if(encryptAlgorithmEnum.getAlgorithmName().equalsIgnoreCase(algorithmName)){
				return encryptAlgorithmEnum;
			}
		}
		
		return null;
	}

}
