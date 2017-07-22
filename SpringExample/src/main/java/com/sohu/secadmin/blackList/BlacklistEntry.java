package com.sohu.secadmin.blackList;

public class BlacklistEntry<E> {

	public String id;
	public E key;
	public int value;
	public int warnLevel = WARN_LEVEL_INVALID; //sogou内部自定义级别

	//将summary中哪些时间段的结果屏蔽
	public long startTime = 0;
	public long endTime = 0;

	public final static int WARN_LEVEL_INVALID = -2;


	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("id:");
		if( id == null ){
			sb.append("_null_");
		} else {
			sb.append(id);
		}
		sb.append(", value:").append(value).append(", warnLevel:").append(warnLevel).append(", key:");
		if( key == null ){
			sb.append("null");
		} else {
			sb.append('[');
//			for( int i=0; i<key.length; i++){
//				if( i > 0 ){
//					sb.append(",");
//				}
//				if( key[i] == null){
//					sb.append("_null_");
//				} else {
//					sb.append( key[i] );
//				}
//			}
			sb.append(']');
		}
		return sb.toString();
	}

}
