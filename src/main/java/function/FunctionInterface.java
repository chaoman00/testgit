package function;

import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Event;
import com.eastrobot.robotnerve.HandlerDef;
import com.eastrobot.robotnerve.InterceptorChain;

/**
 * 二次开发常用功能
 * 
 * @author Mike.Peng
 * @time Aug 26, 2015 6:10:32 PM
 */
public interface FunctionInterface {

	/**
	 * 预处理 将该方法置于chain.doIntercept(channel, event)之前
	 * 
	 * @param channel
	 * @param event
	 */
	public void beforeProcessor(Channel channel, Event event);
	
	
	public void afterProcessor(Channel channel, Event event, HandlerDef handlerDef);

	/**
	 * 消息处理 使用该方法后如果返回为false将不再使用chain.doIntercept(channel, event)
	 *
	 * @param channel
	 * @param event
	 * @return
	 */
	public boolean processor(Channel channel, Event event);

	/**
	 * 消息处理
	 * @param channel
	 * @param event
	 * @param chain
	 * @return
	 */
	public boolean processor(Channel channel, Event event,
                             InterceptorChain chain);

	/**
	 * 后处理 将该方法置于chain.doIntercept(channel, event)之后
	 * 
	 * @param channel
	 * @param event
	 */
	public void afterProcessor(Channel channel, Event event);

}
